package com.ichangemycity.webservice;

import android.app.Activity;

import com.ichangemycity.appdata.ICMyCPreferenceData;

import java.util.HashMap;

public class URLData {
	public static String MAP_API_KEY = "AIzaSyCnZMM9gNma3ke3Ie_vnu_nyFOS56bHF9Q";// "AIzaSyCB_34jNehR_rqMn8DjnrJrNGvhMag-NpE";
	public static final String GCM_SENDER_ID = "933430828390";
	public static final String API_KEY = "af4e61d75d2782a33eac7641e42bba6f";

	// public static final String
	// MAP_KEY_BASE_URL="http://icmycsaasqa.ichangemycity.com/android/";
	// public static final String BASE_URL = 
//	 "http://devapi.ichangemycity.in/engineer/v1/";
//	public static final String BASE_URL = "http://qaapi.ichangemycity.in/engineer/v1/";
	public static final String BASE_URL = "http://api.swachh.city/engineer/v1/";
	public static final String AUTH = "auth";
	public static final String MAP_KEY = "get-map-key";
	public static final String LANGUAGES = "languages";
	public static final String USERS = "engineer";
	public static final String OTP_VERIFICATION = "auth";
	public static final String GENERATE_OTP = "generate-otp";
	public static final String UPDATE_LOCATION = "/location";
	public static final String GENERATE_TOKEN = "/generate-token";
	public static final String UN_ASSIGNED_COMPLAINTS = "complaints/nearByComplaints"; // ULB
	public static final String ASSIGNED_COMPLAINTS_ENGINEER = "complaints/openComplaints";
	public static final String ALL_COMPLAINTS = "complaints/allComplaints";

	public static final String PRIORITY_COMPLAINTS = "complaints/priorityComplaints";
	public static final String ON_THE_JOB_COMPLAINT_LISTS = "complaints/getOnTheJobComplaintLists";
	public static final String REOPENED_COMPLAINT_LISTS = "complaints/getReOpenedComplaintLists";
	public static final String RESOLVED_COMPLAINT_LISTS = "complaints/getResolvedComplaintLists";
	public static final String GET_REJECTED_COMPLAINT_LISTS = "complaints/getRejectedComplaintLists";
	public static final String GET_DESIGNATION_AND_CATEGORIES = "create?lang=";
	public static final String CREATE_ENGINEER = "create";

	public static final String FILE = "file";
	public static final String CATEGORIES = "categories";
	public static final String GET_POSTED_COMPLAINTS = "complaints/posted"; // logged
																			// in
																			// users
	public static final String CHECK_ACTIVE_ENGINEER = "check-active-engineer?apiKey="
			+ API_KEY + "&mobileNumber=";
	public static final String COMPLAINT_POSTED_PROFILE = "complaints/profile-posted-complaint?profileUserId=";// other
																												// users
	public static final String POST_COMPLAINT = "complaint";
	public static final String COMPLAINTS_NEARBY = "complaints/near-by";
	public static final String COMPLAINTS_CITY = "complaints/city";
	public static final String COMPLAINTS_YOURS = "complaints/profile-activity";

	public static final String GET_VOTED_UP_COMPLAINTS = "complaints/voted-up"; // logged
																				// in
																				// users
	public static final String COMPLAINT_VOTED_UP = "complaints/profile-voted-complaint?profileUserId=";// other
																										// users
	public static final String COMPLAINT_ID = "complaint?id=";

	public static final String PROFILE_ACTIVITY = "complaints/profile-activity";
	public static final String GET_POSTED_COMMENT = "comments?complaintId=";
	public static final String GET_POSTED_COMMENT_SORT = "&sortBy=+created_at&perPage=5&page=";
	public static final String GET_VOTED_UP = "vote-ups?complaintId=";
	public static final String GET_VOTED_UP_SORT = "&perPage=5&page=";

	public static final String PAGE = "?page=";
	public static final String _AMB_PAGE = "&page=";
	public static final String COMMENT = "comment";
	public static final String VOTEUP_ON_COMPLAINT = "complaint/voteup";
	public static final String COMPLAINT_STATUS = "complaint-status";
	public static final String FEEDBACK = "feedback";
	public static final String FEEDBACK_STATUS_PUT = "feedback-status";

	public static final String FEEDBACK_OPTIONS = "feedback-options";
	public static final String NOTIFICATION = "/notification";
	public static final String NOTIFICATION_STATUS = "&notificationStatus=";
	public static final String UNREAD = "unRead";
	public static final String READ = "read";
	public static final String SEARCH = "search";
	public static final String KEYWORD = "&keyword=";
	public static final String NOTIFICATION_STATUS_READ = "notification-status";
	public static final String ENGINEER_NOTIFICATION = "engineer/notification";
	// api keys
	public static final String language_id = "language_id";
	public static final String language_name = "language_name";
	public static final String LOGOUT = "/log-out";

	public static final String BEARER_TOKEN = "";
	public static final String CONTENT_TYPE = "application/json";

    public static HashMap<String, String> getHeaders(final Activity activity) {
        String token = "Bearer " + ICMyCPreferenceData.getPreferenceItem(activity, ICMyCPreferenceData.token, "");
        final HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Accept", URLData.CONTENT_TYPE);
        headers.put("Content-Type", URLData.CONTENT_TYPE);
        headers.put("Authorization", token);
        return headers;
    }
}
