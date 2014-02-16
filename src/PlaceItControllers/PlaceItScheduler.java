package PlaceItControllers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.classproj.placeit.iView;
import com.classproj.placeit.PlaceItSettings;

import android.provider.SyncStateContract.Constants;
import android.util.Log;
import Models.PLSchedule;
import Models.PlaceIt;
import PlaceItDB.iPLScheduleModel;
import PlaceItDB.iPlaceItModel;

public class PlaceItScheduler {

	private iPlaceItModel PLrepository;
	private iPLScheduleModel scheduleRepository;
	private iView view;

	public PlaceItScheduler(iPLScheduleModel scheduleDB, iPlaceItModel db, iView view) {
		this.PLrepository = db;
		this.scheduleRepository = scheduleDB;
		this.view = view;
	}

	public void setUpSchedules() {
		List<PlaceIt> placeits = this.PLrepository.getAllPlaceIts();
		for (PlaceIt placeit : placeits) {
			if (placeit.isActive() == true) {
				PLSchedule schedule = this.scheduleRepository.getSchedule(placeit);
				placeit = this.initializeSchedule(placeit, schedule);
			}
		}
	}

	/*
	 * Will modify PLSchedule database and then return a new placeit to be
	 * updated. Given that it has been initialized
	 */
	public PlaceIt initializeSchedule(PlaceIt placeit, PLSchedule schedule) {
		Date currDate = placeit.getActiveDate();
		Calendar cal = Calendar.getInstance();
		int weekday = cal.get(Calendar.DAY_OF_WEEK);
		Calendar min = Calendar.getInstance();
		min.setTime(placeit.getActiveDate());
		min.add(Calendar.YEAR, Integer.MAX_VALUE);
		Date minDate = min.getTime();

		//for (Integer schedule : schedule2) {
			Calendar date = this.nextDayOfWeek(currDate, schedule.getDay());
			date.set(Calendar.HOUR_OF_DAY, 0);
			date.set(Calendar.MINUTE, 0);
			date.set(Calendar.SECOND, 0);
			date.set(Calendar.MILLISECOND, 0);
			Date curr = date.getTime();
			if (minDate.before(curr) == true) {
				minDate = curr;
			}
		//}

		placeit.setActiveDate(minDate.getTime());
		this.PLrepository.updatePlaceIt(placeit);
		return placeit;
	}

	public PlaceIt startSchedule(PlaceIt placeit, PLSchedule schedule) {
		this.addSchedules(placeit, schedule.getDay(), schedule.getWeek());
		return this.initializeSchedule(placeit, schedule);
	}

	/*
	 * Will add schedule to PLSchedule database and return a new placeit to be
	 * updated.
	 */
	public void addSchedules(PlaceIt placeit, int day, int week) {
		this.scheduleRepository.addSchedule(placeit, day, week);
	}

	/* Will remove schedule from placeit and return a new placeit to be updated. */
	public PlaceIt removeSchedule(PlaceIt placeit, int day, int week) {
		return this.scheduleRepository.removeSchedule(placeit, day, week);
	}

	/*
	 * Upon receiving a placeit, it will look for the next scheduled time and
	 * return the place it with the activated date.
	 */
	public PlaceIt scheduleNextActivation(PlaceIt placeit) {
		
		/*PLSchedule schedule = this.scheduleRepository.getSchedule(placeit);
		Log.d("schedules for " + placeit.getID(), schedule.toString());
		if (schedules.contains(0) == false) {
			return this.repostPlaceit(placeit, 
					PlaceItSettings.INTERVAL_TYPE, 
					PlaceItSettings.INTERVAL_NUMBER);
		} else{
			Log.d("IN THE MINUTE SCHEUDLER", "TRUE");
			return this.repostPlaceit(placeit, Calendar.MINUTE, 1);
		}*/
		return null;
	}

	public PlaceIt repostPlaceit(PlaceIt placeit, int TIMEVAL, int timeAMT) {
												  //TIMEVAL IS NOT AN INT!
		java.util.Date date = placeit.getActiveDate();
		int increment = 0;
		if(TIMEVAL == Calendar.MINUTE){
			increment = 60000;
		}else if(Calendar.HOUR == TIMEVAL){
			increment = 60000 * 60;
		}else if (TIMEVAL == Calendar.SECOND){
			increment = 1000;
		}
		else{
			increment = 60000*60*24;
		}
		
		Date newDate = new Date(date.getTime() + increment * timeAMT);
		placeit.setActiveDate(newDate.getTime());
		//Log.d("NEW ACTIVE DATE ", placeit.getActiveDate().toLocaleString());
		Log.d("NEW ACTIVE DATE ", placeit.getActiveDate().toLocaleString());
		this.PLrepository.updatePlaceIt(placeit);
		 return placeit;
	}
	
	
	
	public PlaceIt repostPlaceit(PlaceIt placeit) {
		Calendar cal = Calendar.getInstance();
		cal.add(PlaceItSettings.INTERVAL_TYPE, PlaceItSettings.INTERVAL_NUMBER);
		placeit.setActiveDate(cal.getTime().getTime());
		this.PLrepository.updatePlaceIt(placeit);
		return placeit;
	}
	
	public List<PlaceIt> checkActive(List<PlaceIt> placeits){
		List<PlaceIt> newActive = new Vector<PlaceIt>();
		for(PlaceIt placeit : placeits){
			PlaceIt plDB = this.PLrepository.getPlaceIt(placeit.getID());
			Log.d(plDB.getActiveDate().toLocaleString(), new Date().toLocaleString());
			if(plDB.isActive() && plDB.getActiveDate().getTime() - new Date().getTime() < 0){	
				
				List<Integer> schedules = this.scheduleRepository.getSchedule(plDB);
				Integer day = Calendar.DAY_OF_WEEK;
				if(schedules.size() == 0 || schedules.contains(day)){
					newActive.add(placeit);
				}
				
			}
		}
		view.notifyUser(newActive, "Scheduler");
		return newActive;
	}

	private Calendar nextDayOfWeek(Date curr, int dow) {
		Calendar date = Calendar.getInstance();
		date.setTime(curr);
		int diff = dow - date.get(Calendar.DAY_OF_WEEK);
		if (!(diff > 0)) {
			diff += 7;
		}
		date.add(Calendar.DAY_OF_MONTH, diff);
		return date;
	}
}
