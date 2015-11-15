package co.uk.android.lldc.models;

public class TrailsWayPointModel {

	// "pinLat": 18.9344861,
	// "pinLong": 72.833985,
	// "pinTitle": "Copper Box Arena",
	// "pinImage":
	// "http://dev.pocketapp.co.uk/dev/lldc/uploads/trails/Trail_Entry_Slant_thumbnail.png

	String pinLat = "", pinLong = "", pinTitle = "", pinImage = "";
	String tailsId = "";

	/**
	 * @return the pinLat
	 */
	public String getPinLat() {
		return pinLat;
	}

	/**
	 * @param pinLat
	 *            the pinLat to set
	 */
	public void setPinLat(String pinLat) {
		this.pinLat = pinLat;
	}

	/**
	 * @return the pinLong
	 */
	public String getPinLong() {
		return pinLong;
	}

	/**
	 * @param pinLong
	 *            the pinLong to set
	 */
	public void setPinLong(String pinLong) {
		this.pinLong = pinLong;
	}

	/**
	 * @return the pinTitle
	 */
	public String getPinTitle() {
		return pinTitle;
	}

	/**
	 * @param pinTitle
	 *            the pinTitle to set
	 */
	public void setPinTitle(String pinTitle) {
		this.pinTitle = pinTitle;
	}

	/**
	 * @return the pinImage
	 */
	public String getPinImage() {
		return pinImage;
	}

	/**
	 * @param pinImage
	 *            the pinImage to set
	 */
	public void setPinImage(String pinImage) {
		this.pinImage = pinImage;
	}

	/**
	 * @return the tailsId
	 */
	public String getTailsId() {
		return tailsId;
	}

	/**
	 * @param tailsId
	 *            the tailsId to set
	 */
	public void setTailsId(String tailsId) {
		this.tailsId = tailsId;
	}

}
