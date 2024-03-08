import android.graphics.Bitmap;
import android.util.Base64;

import org.junit.Test;

import static org.junit.Assert.*;

public class PictureTest {

    @Test
    public void testConvertBitmapToJPEG() {
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        byte[] jpeg = Picture.convertBitmapToJPEG(bitmap);
        assertNotNull(jpeg);
        assertTrue(jpeg.length > 0);
    }

    @Test
    public void testConvertBitmapToString() {
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        String imageString = Picture.convertBitmaptoString(bitmap);
        assertNotNull(imageString);
        assertFalse(imageString.isEmpty());
    }

    @Test
    public void testConvertStringToBitmap() {
        String imageString = "Base64EncodedImageString";
        Bitmap bitmap = Picture.convertStringtoBitmap(imageString);
        assertNotNull(bitmap);
    }

    @Test
    public void testConvertStringToJPEG() {
        String imageString = "Base64EncodedImageString";
        byte[] jpeg = Picture.convertStringtoJPEG(imageString);
        assertNotNull(jpeg);
        assertTrue(jpeg.length > 0);
    }

    @Test
    public void testConvertJPEGToBitmap() {
        byte[] jpeg = new byte[100]; // Sample JPEG byte array
        Bitmap bitmap = Picture.convertJPEGtoBitmap(jpeg);
        assertNotNull(bitmap);
    }

    @Test
    public void testConvertJPEGToString() {
        byte[] jpeg = new byte[100]; // Sample JPEG byte array
        String imageString = Picture.convertJPEGtoString(jpeg);
        assertNotNull(imageString);
        assertFalse(imageString.isEmpty());
    }

    @Test
    public void testCreateImage() {
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        String imageString = Picture.convertBitmaptoString(bitmap);
        byte[] jpeg = Picture.convertBitmapToJPEG(bitmap);

        // Test with Bitmap input
        Picture picture1 = Picture.createImage(bitmap);
        assertNotNull(picture1);
        assertEquals(bitmap, picture1.getImage_bitmap());
        assertEquals(imageString, picture1.getImage_string());
        assertArrayEquals(jpeg, picture1.getImage_jpeg());

        // Test with Base64 string input
        Picture picture2 = Picture.createImage(imageString);
        assertNotNull(picture2);
        assertEquals(bitmap, picture2.getImage_bitmap());
        assertEquals(imageString, picture2.getImage_string());
        assertArrayEquals(jpeg, picture2.getImage_jpeg());

        // Test with JPEG byte array input
        Picture picture3 = Picture.createImage(jpeg);
        assertNotNull(picture3);
        assertEquals(bitmap, picture3.getImage_bitmap());
        assertEquals(imageString, picture3.getImage_string());
        assertArrayEquals(jpeg, picture3.getImage_jpeg());
    }

    @Test
    public void testCropToCircle() {
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Bitmap croppedBitmap = Picture.cropToCircle(bitmap);
        assertNotNull(croppedBitmap);
    }
}
