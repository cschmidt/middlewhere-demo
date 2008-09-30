package middlewhere.client.provider;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import middlewhere.client.ItemNumberSupplier;

/**
 * Reads a list of item numbers from a resource file.
 * @author cschmidt
 */
public class DefaultItemNumberSupplier implements ItemNumberSupplier {

    private List<String> itemNumbers;

    public DefaultItemNumberSupplier() {
        itemNumbers = new ArrayList<String>();
        loadItemNumbers();
        itemNumbers = Collections.unmodifiableList(itemNumbers);
    }

    public List<String> getItemNumbers() {
        return itemNumbers;
    }

    private InputStream getItemNumbersInputStream() {
        return getClass().getClassLoader().getResourceAsStream(
                "test-item-numbers.txt");
    }

    private void loadItemNumbers() {
        Scanner scanner = new Scanner(getItemNumbersInputStream());
        while (scanner.hasNext()) {
            itemNumbers.add(scanner.next());
        }
    }
}
