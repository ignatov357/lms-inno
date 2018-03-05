package main.java.NFC_Reader; /**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Marc de Verdelhan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.smartcardio.CardException;

import com.mashape.unirest.http.exceptions.UnirestException;
import main.java.controllers.LoginUsingCardController;
import main.java.NFC_Reader.MifareUtils;
import javafx.application.Platform;
import org.nfctools.mf.MfCardListener;
import org.nfctools.mf.MfReaderWriter;
import org.nfctools.mf.card.MfCard;

/**
 * Entry point of the program.
 * <p>
 * Manager for an ACR122 reader/writer.
 */
public class Acr122Manager {

    public Acr122Manager() {
        this.acr122 = new Acr122Device();
    }

    Acr122Device acr122;
    
    /**
     * Prints information about a card.
     * @param card a card
     */
    private static void printCardInfo(MfCard card) {
        System.out.println("Card detected: "
                + card.getTagType().toString() + " "
                + card.toString());
    }

    /**
     * Starts listening for card
     */
    public void initializeCardListener(LoginUsingCardController controller) throws IOException {
        try {
            this.acr122.open();

            MfCardListener listener = new MfCardListener() {
                @Override
                public void cardDetected(MfCard mfCard, MfReaderWriter mfReaderWriter) throws IOException {
                    printCardInfo(mfCard);
                    Platform.runLater(new Runnable(){
                        @Override
                        public void run() {
                            try {
                                controller.authorizeWithCardUID(MifareUtils.getMifareClassic1KBlock(mfReaderWriter, mfCard, 0, 0, MifareUtils.COMMON_MIFARE_CLASSIC_1K_KEYS));
                            } catch (CardException ce) {
                                System.out.println("Card removed or not present.");
                            } catch (UnirestException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    //System.out.println(MifareUtils.getMifareClassic1KBlock(mfReaderWriter, mfCard, 0, 0, MifareUtils.COMMON_MIFARE_CLASSIC_1K_KEYS));
                }
            };
            acr122.listen(listener);
        } catch (RuntimeException re) {
            throw new RuntimeException("No ACR122 NFC reader found");
        }
    }

    /**
     * Terminates listener
     * @throws IOException
     */
    public void terminateListener() throws IOException {
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                try {
                    acr122.close();
                } catch (Exception e) {

                }
            }
        });
    }
}
