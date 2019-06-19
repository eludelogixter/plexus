package brood.com.medcrawler;

import android.annotation.TargetApi;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.Page;
import android.graphics.pdf.PdfDocument.PageInfo;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@TargetApi(19)
/**
 * Class used to generate a pdf and share it via email
 */
class AdminPDFGen {

    /********************** Constants *************************/

    // page, page margins and card dimensions
    // 1 inch = 72 pixels
    private static final int PAGE_WIDTH = 612;
    private static final int PAGE_HEIGHT = 792;
    private static final int LR_PAGE_MARGIN = 54; // left or right page margin
    private static final int TB_PAGE_MARGIN = 36; // top or bottom page margin
    private static final int CARD_WIDTH = 252;
    private static final int CARD_HEIGHT = 144;

    private static final int NUM_CARDS_PAGE = 10; // maximum number of cards per page

    // Key and category texts color in hexadecimal.
    // The format is #RRGGBB (RR -> red, GG -> Green, BB -> Blue)
    private static final String KEY_TEXT_COLOR = "#3D3D3D";
    private static final String CAT_TEXT_COLOR = "#3D3D3D";

    // key and category texts size in pixels
    private static final int KEY_TEXT_SIZE = 14;
    private static final int CAT_TEXT_SIZE = 14;

    private static final int TEXTS_SPACE = 10; //space between key text and category text

    //********************* Instance variables ****************************
    private PdfDocument document;
    private List<KeyRow> keys;
    private Paint paintKey;
    private Paint paintCat;

    /**
     * Constructor
     * @param keys list with the keys used to generate a pdf document
     */
    public AdminPDFGen(List<KeyRow> keys){
        document = new PdfDocument();
        this.keys = keys;

        paintKey = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCat = new Paint(Paint.ANTI_ALIAS_FLAG);

        // text color
        paintKey.setColor(Color.parseColor(KEY_TEXT_COLOR));
        paintCat.setColor(Color.parseColor(CAT_TEXT_COLOR));

        // text size in pixels
        paintKey.setTextSize(KEY_TEXT_SIZE);
        paintCat.setTextSize(CAT_TEXT_SIZE);

        // text shadow
        //paintKey.setShadowLayer(1f, 0f, 1f, Color.WHITE);
        //paintCat.setShadowLayer(1f, 0f, 1f, Color.WHITE);

    }

    /**
     * Creates a pdf file with keys from keys list which is ready to print it in a printable business card and
     * opens the email app and automatically attaches the file to the email
     *
     * @param originActivity activity from where this method is called
     * @return pdf file - if the operation is not successful it returns null, otherwise returns
     *                    the pdf file which has to be deleted by the caller of this method
     */
    public File generateKeysPDF(){

        File file = null; // temporary pdf file

        // ensures that the keys list has at least one element
        if(keys.size() > 0) {

            int fullPageNumber = keys.size() / NUM_CARDS_PAGE; // calculates the number of pages filled
            // totally with cards

            int numCardsLastPage = keys.size() % NUM_CARDS_PAGE; // calculates the number of cards on the
            // last page if the last page is not
            // totally filled with cards

            // create a new document
            document = new PdfDocument();

            int i;

            // for each page full of cards is called the private method drawCardsOnPage
            // to write the pdf page
            for (i = 0; i < fullPageNumber; i++) {

                drawCardsOnPage((i + 1), NUM_CARDS_PAGE);

            }

            // if the last page is not totally filled with cards, draw these cards on the last pdf page
            if (numCardsLastPage != 0) {
                drawCardsOnPage((i + 1), numCardsLastPage);
            }

            FileOutputStream fileOutputStream = null;

            try {

                file = File.createTempFile("keys", ".pdf",
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));

                fileOutputStream = new FileOutputStream(file);

                // write the document content
                document.writeTo(fileOutputStream);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                file = null;
            } catch (IOException e) {
                e.printStackTrace();
                file = null;
            } finally {
                try {
                    if(fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // close the document
            document.close();

//            if (file != null) {
//
//                file = null;
//            }

        }

        return file;

    }

    /**
     * Private method which draws cards on a pdf page. Each card contains a key and the specialty
     * associated to the key. The maximum cards on a page is 10. The cards are distributed in
     * 2 columns each one with 5 cards.
     *
     * @param pageNum the page number of the pdf document
     * @param nCards number of cards to be written on the pdf page
     */
    private void drawCardsOnPage(int pageNum, int nCards){

        // crate a page description
        PageInfo pageInfo = new PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNum).create();

        // start a page
        Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Rect bounds = new Rect();

        // variables used in the for loop
        // x -> x coordinate of the canvas
        // y -> y coordinate of the canvas
        // cardCol -> the card column (0 or 1)
        // cardRow -> the card row (0 to 4)
        // index -> index of the card in the keys list
        int x, y, cardCol, cardRow, index;

        // loop which draws cards on a page
        for(int i = 0; i < nCards; i++){

            // calculates the index
            index = i + ((pageNum -1) * NUM_CARDS_PAGE);

            // get the key and category of a card
            String sKey = keys.get(index).getKey();
            String sCat = keys.get(index).getUserCategory();

            // takes the bounds of the key text
            paintKey.getTextBounds(sKey, 0, sKey.length(), bounds);

            // calculates the row and column of a card
            cardCol = (i % 2 == 0) ? 0 : 1;
            cardRow = i / 2;

            // calculates x and y coordinates where to draw the key on the card
            x =  ((CARD_WIDTH - bounds.width()) / 2) + LR_PAGE_MARGIN + cardCol * CARD_WIDTH;
            y = ((CARD_HEIGHT + bounds.height()) / 2) + TB_PAGE_MARGIN + cardRow * CARD_HEIGHT - TEXTS_SPACE;

            // draws the key on a the card
            canvas.drawText(sKey, x, y, paintKey);

            // takes the bounds of the category text
            paintCat.getTextBounds(sCat, 0, sCat.length(), bounds);

            // calculates x and y coordinates where to draw the category on the card
            x = ((CARD_WIDTH - bounds.width()) / 2) + LR_PAGE_MARGIN + cardCol * CARD_WIDTH;
            y = ((CARD_HEIGHT + bounds.height()) / 2) + TB_PAGE_MARGIN + cardRow * CARD_HEIGHT + TEXTS_SPACE;

            // draws the category on a the card
            canvas.drawText(sCat, x, y, paintCat);
        }

        // finish the page
        document.finishPage(page);
    }

}
