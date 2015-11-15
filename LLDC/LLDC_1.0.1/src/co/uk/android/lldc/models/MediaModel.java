package co.uk.android.lldc.models;

public class MediaModel {

	String imageUrl;
	long _id, mediaType;

	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * @param imageUrl
	 *            the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * @return the _id
	 */
	public long get_id() {
		return _id;
	}

	/**
	 * @param _id
	 *            the _id to set
	 */
	public void set_id(long _id) {
		this._id = _id;
	}

	/**
	 * @return the mediaType
	 */
	public long getMediaType() {
		return mediaType;
	}

	/**
	 * @param mediaType
	 *            the mediaType to set
	 */
	public void setMediaType(long mediaType) {
		this.mediaType = mediaType;
	}

}
