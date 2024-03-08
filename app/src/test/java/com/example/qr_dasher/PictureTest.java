import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class PictureTest {

    @Test
    public void testConvertBitmapToJPEG() {
        Bitmap bitmap = getTestBitmap();
        assertNotNull(bitmap);

        byte[] jpeg = Picture.convertBitmapToJPEG(bitmap);
        assertNotNull(jpeg);
        assertTrue(jpeg.length > 0);
    }

    @Test
    public void testConvertBitmapToString() {
        Bitmap bitmap = getTestBitmap();
        assertNotNull(bitmap);

        String base64String = Picture.convertBitmaptoString(bitmap);
        assertNotNull(base64String);
        assertFalse(base64String.isEmpty());
    }

    @Test
    public void testConvertStringToBitmap() {
        String base64String = "YOUR_BASE64_STRING_HERE"; // Provide a valid Base64 string
        assertNotNull(base64String);

        Bitmap bitmap = Picture.convertStringtoBitmap(base64String);
        assertNotNull(bitmap);
    }

    @Test
    public void testConvertStringToJPEG() {
        String base64String = "YOUR_BASE64_STRING_HERE"; // Provide a valid Base64 string
        assertNotNull(base64String);

        byte[] jpeg = Picture.convertStringtoJPEG(base64String);
        assertNotNull(jpeg);
        assertTrue(jpeg.length > 0);
    }

    @Test
    public void testConvertJPEGToBitmap() {
        byte[] jpeg = new byte[1024]; // Provide a valid JPEG byte array
        assertNotNull(jpeg);

        Bitmap bitmap = Picture.convertJPEGtoBitmap(jpeg);
        assertNotNull(bitmap);
    }

    @Test
    public void testConvertJPEGToString() {
        byte[] jpeg = new byte[1024]; // Provide a valid JPEG byte array
        assertNotNull(jpeg);

        String base64String = Picture.convertJPEGtoString(jpeg);
        assertNotNull(base64String);
        assertFalse(base64String.isEmpty());
    }

    // Helper method to load a test Bitmap from a resource
    private Bitmap getTestBitmap() {
        // Load the test Bitmap from a resource (replace "test_image.png" with your image file)
        Bitmap bitmap = BitmapFactory.decodeResource(
                getClass().getClassLoader().getResourceAsStream("test_image.png"));
        assertNotNull("Test bitmap is null", bitmap);
        return bitmap;
    }
}
