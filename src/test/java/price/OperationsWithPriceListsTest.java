package price;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Класс тестов для операций с ценами
 */
public class OperationsWithPriceListsTest {
    /*@Test
    public void unionPrices() throws Exception {
        List<Price> currentPriceList = new LinkedList<>();
        List<Price> importedPriceList = new LinkedList<>();

        currentPriceList.add(new Price(1, "122856", 1, 1,
                new Date(2013, 0, 1, 0, 0, 0),
                new Date(2013, 0, 31, 23, 59, 59),
                11000));
        currentPriceList.add(new Price(2, "122856", 2, 1,
                new Date(2013, 0, 10, 0, 0, 0),
                new Date(2013, 0, 20, 23, 59, 59),
                99000));
        currentPriceList.add(new Price(3, "6654", 1, 2,
                new Date(2013, 0, 1, 0, 0, 0),
                new Date(2013, 0, 31, 0, 0, 0),
                5000));

        importedPriceList.add(new Price(4, "122856", 1, 1,
                new Date(2013, 0, 20, 0, 0, 0),
                new Date(2013, 1, 20, 23, 59, 59),
                11000));
        importedPriceList.add(new Price(5, "122856", 2, 1,
                new Date(2013, 0, 15, 0, 0, 0),
                new Date(2013, 0, 25, 23, 59, 59),
                92000));
        importedPriceList.add(new Price(6, "6654", 1, 2,
                new Date(2013, 0, 12, 0, 0, 0),
                new Date(2013, 0, 13, 0, 0, 0),
                4000));

        OperationsWithPriceLists operationsWithPriceLists = new OperationsWithPriceLists();
        System.out.println(operationsWithPriceLists.mergePriceLists(currentPriceList, importedPriceList));

        Price price = new Price(6, "6654", 1, 2,
                new Date(2013, 0, 12, 0, 0, 0),
                new Date(2013, 0, 13, 0, 0, 0),
                4000);
        Price price1 = new Price(6, "6654", 1, 2,
                new Date(2013, 0, 12, 0, 0, 0),
                new Date(2013, 0, 13, 0, 0, 0),
                4000);
        System.out.print(price.equals(price1));
    }*/

    /**
     * Объединение цен с одинаковым значением цен
     * Текущая цена:                01.01.2013     31.01.2013       11000
     * Импортируемая цена:          20.01.2013     20.02.2013       11000
     * Предполагаемый результат:    01.01.2013     20.02.2013       11000
     * @throws Exception
     */
    @Test
    public void unionPricesWithSameValue() throws Exception {
        List<Price> currentPriceList = new LinkedList<>();
        List<Price> importedPriceList = new LinkedList<>();
        List<Price> expectedResult = new LinkedList<>();

        currentPriceList.add(new Price(1, "122856", 1, 1,
                new Date(2013, 0, 1, 0, 0, 0),
                new Date(2013, 0, 31, 23, 59, 59),
                11000));

        importedPriceList.add(new Price(4, "122856", 1, 1,
                new Date(2013, 0, 20, 0, 0, 0),
                new Date(2013, 1, 20, 23, 59, 59),
                11000));

        expectedResult.add(new Price(1, "122856", 1, 1,
                new Date(2013, 0, 1, 0, 0, 0),
                new Date(2013, 1, 20, 23, 59, 59),
                11000));

        OperationsWithPriceLists operationsWithPriceLists = new OperationsWithPriceLists();
        Assert.assertEquals(expectedResult, operationsWithPriceLists.mergePriceLists(currentPriceList, importedPriceList));
    }

    /**
     * Объединение цен с различным значением цен (диапазоны цен пересекаются, диапазон текущей цены начинается раньше)
     * Текущая цена:                10.01.2013     20.01.2013       99000
     * Импортируемая цена:          15.01.2013     25.01.2013       92000
     * Предполагаемый результат:    10.01.2013     15.01.2013       99000
     *                              15.01.2013     25.01.2013       92000
     * @throws Exception
     */
    @Test
    public void unionPricesWithoutSameValue() throws Exception {
        List<Price> currentPriceList = new LinkedList<>();
        List<Price> importedPriceList = new LinkedList<>();
        List<Price> expectedResult = new LinkedList<>();

        currentPriceList.add(new Price(2, "122856", 2, 1,
                new Date(2013, 0, 10, 0, 0, 0),
                new Date(2013, 0, 20, 23, 59, 59),
                99000));

        importedPriceList.add(new Price(5, "122856", 2, 1,
                new Date(2013, 0, 15, 0, 0, 0),
                new Date(2013, 0, 25, 23, 59, 59),
                92000));

        expectedResult.add(new Price(2, "122856", 2, 1,
                new Date(2013, 0, 10, 0, 0, 0),
                new Date(2013, 0, 15, 0, 0, 0),
                99000));
        expectedResult.add(new Price(5, "122856", 2, 1,
                new Date(2013, 0, 15, 0, 0, 0),
                new Date(2013, 0, 25, 23, 59, 59),
                92000));

        OperationsWithPriceLists operationsWithPriceLists = new OperationsWithPriceLists();
        Assert.assertEquals(expectedResult, operationsWithPriceLists.mergePriceLists(currentPriceList, importedPriceList));
    }

    /**
     * Объединение цен с различным значением цен (диапазон импортируемой цены расположен в диапазоне текущей цены)
     * Текущая цена:                01.01.2013     31.01.2013       5000
     * Импортируемая цена:          12.01.2013     13.01.2013       4000
     * Предполагаемый результат:    01.01.2013     12.01.2013       5000
     *                              12.01.2013     13.01.2013       4000
     *                              13.01.2013     31.01.2013       5000
     * @throws Exception
     */
    @Test
    public void unionPrices() throws Exception {
        List<Price> currentPriceList = new LinkedList<>();
        List<Price> importedPriceList = new LinkedList<>();
        List<Price> expectedResult = new LinkedList<>();

        currentPriceList.add(new Price(3, "6654", 1, 2,
                new Date(2013, 0, 1, 0, 0, 0),
                new Date(2013, 0, 31, 0, 0, 0),
                5000));

        importedPriceList.add(new Price(6, "6654", 1, 2,
                new Date(2013, 0, 12, 0, 0, 0),
                new Date(2013, 0, 13, 0, 0, 0),
                4000));

        expectedResult.add(new Price(3, "6654", 1, 2,
                new Date(2013, 0, 1, 0, 0, 0),
                new Date(2013, 0, 12, 0, 0, 0),
                5000));
        expectedResult.add(new Price(6, "6654", 1, 2,
                new Date(2013, 0, 12, 0, 0, 0),
                new Date(2013, 0, 13, 0, 0, 0),
                4000));
        expectedResult.add(new Price(3, "6654", 1, 2,
                new Date(2013, 0, 13, 0, 0, 0),
                new Date(2013, 0, 31, 0, 0, 0),
                5000));

        OperationsWithPriceLists operationsWithPriceLists = new OperationsWithPriceLists();
        Assert.assertEquals(expectedResult, operationsWithPriceLists.mergePriceLists(currentPriceList, importedPriceList));
    }
}