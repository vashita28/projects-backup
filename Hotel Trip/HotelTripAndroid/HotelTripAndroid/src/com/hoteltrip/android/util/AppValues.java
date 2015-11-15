package com.hoteltrip.android.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.hoteltrip.android.FindHotelActivity;
import com.hoteltrip.android.R;

public class AppValues {

	public static String ms_szURL = "http://dev.pocketapp.co.uk/dev/hotel-trip/index.php/";
	// "http://192.168.1.38/hotel-trip/index.php/"

	public static String ms_szSelectedDestination = "";
	public static String ms_szSearchHotelResponse = "";
	public static ArrayList<HotelData> hotelDataList;

	public static String szLoggedInEmail = "";
	public static String szName = "";
	public static String szSurName = "";
	public static String szPaxPassport = "";
	public static String szPrefix = "";

	public static String szMemberName = "";
	public static String szMemberSurname = "";
	public static String szMemberPrefix = "";

	public static String szUserName = "";
	public static String szPassword = "";
	public static String szAgentID = "";;
	
	public static String szDestCity="";
	public static String szDestCountry="";
	public static List<FindHotelActivity.RoomOccupancyDetails> roomListDetails;

	public static String szRoomInfo = "";
	public static String szCheckInDate = "";
	public static String szCheckOutDate = "";
	public static Calendar checkinCalendar;
	public static int nNumberOfNights = 1;
	public static String szCurrency = "";

	public static String szChargedTo = "";
	public static String szContactPhone = "";

	public static int nNumberOfRooms = 1;
	public static int nNumberOfAdults = 1;

	public static int nSortType = R.id.cb_priceasc;

	public static String ms_szBookingResponse = "{\r\n"
			+ "    \"BookHotel_Response\": {\r\n"
			+ "        \"ResNo\": \"HTMBMA140200008\",\r\n"
			+ "        \"CompleteService\": {\r\n"
			+ "            \"HBookId\": {\r\n"
			+ "                \"@attributes\": {\r\n"
			+ "                    \"CanAmend\": \"Y\",\r\n"
			+ "                    \"Id\": \"HBMA1402000008\",\r\n"
			+ "                    \"VoucherNo\": \"VBMA1402000008\",\r\n"
			+ "                    \"VoucherDt\": \"2014-02-17\",\r\n"
			+ "                    \"Status\": \"CONF\",\r\n"
			+ "                    \"InternalCode\": \"CL098\"\r\n"
			+ "                },\r\n"
			+ "                \"HotelId\": \"WSMA0511000174\",\r\n"
			+ "                \"Period\": {\r\n"
			+ "                    \"@attributes\": {\r\n"
			+ "                        \"FromDt\": \"2014-03-21\",\r\n"
			+ "                        \"ToDt\": \"2014-03-22\"\r\n"
			+ "                    }\r\n"
			+ "                },\r\n"
			+ "                \"RoomCatgInfo\": {\r\n"
			+ "                    \"RoomCatg\": {\r\n"
			+ "                        \"@attributes\": {\r\n"
			+ "                            \"CatgId\": \"WSMA05110039\",\r\n"
			+ "                            \"CatgName\": \"STANDARD\",\r\n"
			+ "                            \"Market\": \"World Wide\",\r\n"
			+ "                            \"Avail\": \"Y\",\r\n"
			+ "                            \"BFType\": \"RO\"\r\n"
			+ "                        },\r\n"
			+ "                        \"Room\": {\r\n"
			+ "                            \"SeqRoom\": [{\r\n"
			+ "                                \"@attributes\": {\r\n"
			+ "                                    \"ServiceNo\": \"MA1402000011\",\r\n"
			+ "                                    \"RoomType\": \"Triple\",\r\n"
			+ "                                    \"SeqNo\": \"1\",\r\n"
			+ "                                    \"AdultNum\": \"3\",\r\n"
			+ "                                    \"ChildAge1\": \"11\",\r\n"
			+ "                                    \"ChildAge2\": \"8\"\r\n"
			+ "                                },\r\n"
			+ "                                \"PriceInfomation\": {\r\n"
			+ "                                    \"NightPrice\": {\r\n"
			+ "                                        \"@attributes\": {\r\n"
			+ "                                            \"Offset\": \"0\"\r\n"
			+ "                                        },\r\n"
			+ "                                        \"Accom\": {\r\n"
			+ "                                            \"@attributes\": {\r\n"
			+ "                                                \"Price\": \"4700.00\"\r\n"
			+ "                                            }\r\n"
			+ "                                        },\r\n"
			+ "                                        \"Child\": {\r\n"
			+ "                                            \"@attributes\": {\r\n"
			+ "                                                \"MinAge\": \"0\",\r\n"
			+ "                                                \"MaxAge\": \"12\",\r\n"
			+ "                                                \"Info\": \"\"\r\n"
			+ "                                            }\r\n"
			+ "                                        },\r\n"
			+ "                                        \"Minstay\": {\r\n"
			+ "                                            \"@attributes\": {\r\n"
			+ "                                                \"MSDay\": \"0.00\",\r\n"
			+ "                                                \"MSType\": \"NONE\",\r\n"
			+ "                                                \"MSRate\": \"0.00\",\r\n"
			+ "                                                \"MSPrice\": \"0.00\"\r\n"
			+ "                                            }\r\n"
			+ "                                        },\r\n"
			+ "                                        \"Compulsory\": {\r\n"
			+ "                                            \"@attributes\": {\r\n"
			+ "                                                \"Name\": \"NONE\",\r\n"
			+ "                                                \"Price\": \"0.00\"\r\n"
			+ "                                            }\r\n"
			+ "                                        },\r\n"
			+ "                                        \"Supplement\": {\r\n"
			+ "                                            \"@attributes\": {\r\n"
			+ "                                                \"Name\": \"NONE\",\r\n"
			+ "                                                \"Price\": \"0.00\"\r\n"
			+ "                                            }\r\n"
			+ "                                        },\r\n"
			+ "                                        \"Promotion\": {\r\n"
			+ "                                            \"@attributes\": {\r\n"
			+ "                                                \"Name\": \"NONE\",\r\n"
			+ "                                                \"Value\": \"False\",\r\n"
			+ "                                                \"BFPrice\": \"0\",\r\n"
			+ "                                                \"PromoCode\": \"\"\r\n"
			+ "                                            }\r\n"
			+ "                                        },\r\n"
			+ "                                        \"EarlyBird\": {\r\n"
			+ "                                            \"@attributes\": {\r\n"
			+ "                                                \"EBType\": \"NONE\",\r\n"
			+ "                                                \"EBRate\": \"0.00\",\r\n"
			+ "                                                \"EBPrice\": \"0.00\"\r\n"
			+ "                                            }\r\n"
			+ "                                        },\r\n"
			+ "                                        \"Commission\": {\r\n"
			+ "                                            \"@attributes\": {\r\n"
			+ "                                                \"CommType\": \"NONE\",\r\n"
			+ "                                                \"CommRate\": \"0.00\",\r\n"
			+ "                                                \"CommPrice\": \"0.00\"\r\n"
			+ "                                            }\r\n"
			+ "                                        }\r\n"
			+ "                                    }\r\n"
			+ "                                },\r\n"
			+ "                                \"TotalPrice\": \"4700.00\",\r\n"
			+ "                                \"CommissionPrice\": \"0.00\",\r\n"
			+ "                                \"NetPrice\": \"4700.00\",\r\n"
			+ "                                \"PaxInformation\": {\r\n"
			+ "                                    \"GuestName\": [\" kanad, Mr.\", \" kanad, Mr.\", \" kanad, Mr.\"]\r\n"
			+ "                                }\r\n"
			+ "                            }, {\r\n"
			+ "                                \"@attributes\": {\r\n"
			+ "                                    \"ServiceNo\": \"MA1402000012\",\r\n"
			+ "                                    \"RoomType\": \"Triple\",\r\n"
			+ "                                    \"SeqNo\": \"2\",\r\n"
			+ "                                    \"AdultNum\": \"2\",\r\n"
			+ "                                    \"ChildAge1\": \"4\",\r\n"
			+ "                                    \"ChildAge2\": \"3\"\r\n"
			+ "                                },\r\n"
			+ "                                \"PriceInfomation\": {\r\n"
			+ "                                    \"NightPrice\": {\r\n"
			+ "                                        \"@attributes\": {\r\n"
			+ "                                            \"Offset\": \"0\"\r\n"
			+ "                                        },\r\n"
			+ "                                        \"Accom\": {\r\n"
			+ "                                            \"@attributes\": {\r\n"
			+ "                                                \"Price\": \"4050.00\"\r\n"
			+ "                                            }\r\n"
			+ "                                        },\r\n"
			+ "                                        \"Child\": {\r\n"
			+ "                                            \"@attributes\": {\r\n"
			+ "                                                \"MinAge\": \"0\",\r\n"
			+ "                                                \"MaxAge\": \"12\",\r\n"
			+ "                                                \"Info\": \"\"\r\n"
			+ "                                            }\r\n"
			+ "                                        },\r\n"
			+ "                                        \"Minstay\": {\r\n"
			+ "                                            \"@attributes\": {\r\n"
			+ "                                                \"MSDay\": \"0.00\",\r\n"
			+ "                                                \"MSType\": \"NONE\",\r\n"
			+ "                                                \"MSRate\": \"0.00\",\r\n"
			+ "                                                \"MSPrice\": \"0.00\"\r\n"
			+ "                                            }\r\n"
			+ "                                        },\r\n"
			+ "                                        \"Compulsory\": {\r\n"
			+ "                                            \"@attributes\": {\r\n"
			+ "                                                \"Name\": \"NONE\",\r\n"
			+ "                                                \"Price\": \"0.00\"\r\n"
			+ "                                            }\r\n"
			+ "                                        },\r\n"
			+ "                                        \"Supplement\": {\r\n"
			+ "                                            \"@attributes\": {\r\n"
			+ "                                                \"Name\": \"NONE\",\r\n"
			+ "                                                \"Price\": \"0.00\"\r\n"
			+ "                                            }\r\n"
			+ "                                        },\r\n"
			+ "                                        \"Promotion\": {\r\n"
			+ "                                            \"@attributes\": {\r\n"
			+ "                                                \"Name\": \"NONE\",\r\n"
			+ "                                                \"Value\": \"False\",\r\n"
			+ "                                                \"BFPrice\": \"0\",\r\n"
			+ "                                                \"PromoCode\": \"\"\r\n"
			+ "                                            }\r\n"
			+ "                                        },\r\n"
			+ "                                        \"EarlyBird\": {\r\n"
			+ "                                            \"@attributes\": {\r\n"
			+ "                                                \"EBType\": \"NONE\",\r\n"
			+ "                                                \"EBRate\": \"0.00\",\r\n"
			+ "                                                \"EBPrice\": \"0.00\"\r\n"
			+ "                                            }\r\n"
			+ "                                        },\r\n"
			+ "                                        \"Commission\": {\r\n"
			+ "                                            \"@attributes\": {\r\n"
			+ "                                                \"CommType\": \"NONE\",\r\n"
			+ "                                                \"CommRate\": \"0.00\",\r\n"
			+ "                                                \"CommPrice\": \"0.00\"\r\n"
			+ "                                            }\r\n"
			+ "                                        }\r\n"
			+ "                                    }\r\n"
			+ "                                },\r\n"
			+ "                                \"TotalPrice\": \"4050.00\",\r\n"
			+ "                                \"CommissionPrice\": \"0.00\",\r\n"
			+ "                                \"NetPrice\": \"4050.00\",\r\n"
			+ "                                \"PaxInformation\": {\r\n"
			+ "                                    \"GuestName\": [\" kanad, Mr.\", \" kanad, Mr.\", \" kanad, Mr.\"]\r\n"
			+ "                                }\r\n"
			+ "                            }]\r\n"
			+ "                        }\r\n" + "                    }\r\n"
			+ "                },\r\n"
			+ "                \"RequestDes\": {},\r\n"
			+ "                \"Message\": {}\r\n" + "            }\r\n"
			+ "        },\r\n" + "        \"UnCompleteService\": {}\r\n"
			+ "    }\r\n" + "}";

	public static Location getLocation(Context context) {
		Location location = null;
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		if (locationManager != null) {
			location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}
		if (location == null) {
			location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}

		return location;
	}
}
