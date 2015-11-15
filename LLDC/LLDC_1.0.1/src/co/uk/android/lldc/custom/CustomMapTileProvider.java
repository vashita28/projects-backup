package co.uk.android.lldc.custom;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.util.Log;

import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;

public class CustomMapTileProvider implements TileProvider {
	private static final String TAG = "CustomMapTileProvider";

	private static final int TILE_WIDTH = 256;
	private static final int TILE_HEIGHT = 256;
	private static final int BUFFER_SIZE = 16 * 1024;

	private AssetManager mAssets;

	public CustomMapTileProvider(AssetManager assets) {
		mAssets = assets;
	}

	@Override
	public Tile getTile(int x, int y, int zoom) {
		byte[] image = readTileImage(x, y, zoom);
		return image == null ? null : new Tile(TILE_WIDTH, TILE_HEIGHT, image);
	}

	private byte[] readTileImage(int x, int y, int zoom) {
		InputStream in = null;
		ByteArrayOutputStream buffer = null;

		try {
			in = mAssets.open(getTileFilename(x, y, zoom));
			buffer = new ByteArrayOutputStream();

			int nRead;
			byte[] data = new byte[BUFFER_SIZE];

			while ((nRead = in.read(data, 0, BUFFER_SIZE)) != -1) {
				buffer.write(data, 0, nRead);
			}
			buffer.flush();

			return buffer.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (Exception ignored) {
				}
			if (buffer != null)
				try {
					buffer.close();
				} catch (Exception ignored) {
				}
		}
	}

	private String getTileFilename(int x, int y, int zoom) {

		String szUrl = "";

		if (zoom == 15 && (x >= 16380 && x <= 16385)
				&& (y >= 10888 && y <= 10893)) {
			szUrl = "map/" + zoom + '/' + x + '/' + y + ".png";
		} else if (zoom == 16 && (x >= 32760 && x <= 32770)
				&& (y >= 21776 && y <= 21886)) {
			szUrl = "map/" + zoom + '/' + x + '/' + y + ".png";
		} else if (zoom == 17 && (x >= 65521 && x <= 65540)
				&& (y >= 43553 && y <= 43572)) {
			szUrl = "map/" + zoom + '/' + x + '/' + y + ".png";
		} else {
			szUrl = "map/blank.png";
		}

		Log.e(TAG, "szUrl:: " + szUrl);
		return szUrl;
	}
}
