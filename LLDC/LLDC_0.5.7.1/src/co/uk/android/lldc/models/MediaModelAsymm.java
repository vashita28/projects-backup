package co.uk.android.lldc.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;

public class MediaModelAsymm implements AsymmetricItem {
	private int columnSpan;
	private int rowSpan;
	private int position;

	private String imageUrl = "";

	public MediaModelAsymm() {
		this(1, 1, 0);
	}

	public MediaModelAsymm(final int columnSpan, final int rowSpan, int position) {
		this.columnSpan = columnSpan;
		this.rowSpan = rowSpan;
		this.position = position;
	}

	public MediaModelAsymm(final Parcel in) {
		readFromParcel(in);
	}

	@Override
	public int getColumnSpan() {
		return columnSpan;
	}

	@Override
	public int getRowSpan() {
		return rowSpan;
	}

	public int getPosition() {
		return position;
	}

	@Override
	public String toString() {
		return String.format("%s: %sx%s", position, rowSpan, columnSpan);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	private void readFromParcel(final Parcel in) {
		columnSpan = in.readInt();
		rowSpan = in.readInt();
		position = in.readInt();
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		dest.writeInt(columnSpan);
		dest.writeInt(rowSpan);
		dest.writeInt(position);
	}

	/* Parcelable interface implementation */
	public static final Parcelable.Creator<MediaModelAsymm> CREATOR = new Parcelable.Creator<MediaModelAsymm>() {

		@Override
		public MediaModelAsymm createFromParcel(final Parcel in) {
			return new MediaModelAsymm(in);
		}

		@Override
		public MediaModelAsymm[] newArray(final int size) {
			return new MediaModelAsymm[size];
		}
	};

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

}
