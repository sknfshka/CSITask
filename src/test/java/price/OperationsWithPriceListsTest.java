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

    /**
     * Объединение списка цен
     * Текущая цена:                10.01.2013     20.01.2013       100
     *                              20.01.2013     25.01.2013       120
     * Импортируемая цена:          18.01.2013     22.01.2013       110
     * Предполагаемый результат:    10.01.2013     18.01.2013       100
     *                              18.01.2013     22.01.2013       110
     *                              22.01.2013     25.01.2013       120
     * @throws Exception
     */
    @Test
    public void unionPricesLists() throws Exception {
        List<Price> currentPriceList = new LinkedList<>();
        List<Price> importedPriceList = new LinkedList<>();
        List<Price> expectedResult = new LinkedList<>();

        currentPriceList.add(new Price(1, "6654", 1, 2,
                new Date(2013, 0, 10, 0, 0, 0),
                new Date(2013, 0, 20, 0, 0, 0),
                100));
        currentPriceList.add(new Price(2, "6654", 1, 2,
                new Date(2013, 0, 20, 0, 0, 0),
                new Date(2013, 0, 25, 0, 0, 0),
                120));

        importedPriceList.add(new Price(3, "6654", 1, 2,
                new Date(2013, 0, 18, 0, 0, 0),
                new Date(2013, 0, 22, 0, 0, 0),
                110));

        expectedResult.add(new Price(1, "6654", 1, 2,
                new Date(2013, 0, 10, 0, 0, 0),
                new Date(2013, 0, 18, 0, 0, 0),
                100));
        expectedResult.add(new Price(3, "6654", 1, 2,
                new Date(2013, 0, 18, 0, 0, 0),
                new Date(2013, 0, 22, 0, 0, 0),
                110));
        expectedResult.add(new Price(2, "6654", 1, 2,
                new Date(2013, 0, 22, 0, 0, 0),
                new Date(2013, 0, 25, 0, 0, 0),
                120));

        OperationsWithPriceLists operationsWithPriceLists = new OperationsWithPriceLists();
        Assert.assertEquals(expectedResult, operationsWithPriceLists.mergePriceLists(currentPriceList, importedPriceList));
    }

    /**
     * Объединение списка цен
     * Текущая цена:                10.01.2013     15.01.2013       80
     *                              15.01.2013     20.01.2013       87
     *                              20.01.2013     25.01.2013       90
     * Импортируемая цена:          12.01.2013     17.01.2013       80
     *                              17.01.2013     22.01.2013       85
     * Предполагаемый результат:    10.01.2013     17.01.2013       80
     *                              17.01.2013     22.01.2013       85
     *                              22.01.2013     25.01.2013       90
     * @throws Exception
     */
    @Test
    public void unionOtherPricesLists() throws Exception {
        List<Price> currentPriceList = new LinkedList<>();
        List<Price> importedPriceList = new LinkedList<>();
        List<Price> expectedResult = new LinkedList<>();

        currentPriceList.add(new Price(1, "6654", 1, 2,
                new Date(2013, 0, 10, 0, 0, 0),
                new Date(2013, 0, 15, 0, 0, 0),
                80));
        currentPriceList.add(new Price(2, "6654", 1, 2,
                new Date(2013, 0, 15, 0, 0, 0),
                new Date(2013, 0, 20, 0, 0, 0),
                87));
        currentPriceList.add(new Price(3, "6654", 1, 2,
                new Date(2013, 0, 20, 0, 0, 0),
                new Date(2013, 0, 25, 0, 0, 0),
                90));

        importedPriceList.add(new Price(4, "6654", 1, 2,
                new Date(2013, 0, 12, 0, 0, 0),
                new Date(2013, 0, 17, 0, 0, 0),
                80));
        importedPriceList.add(new Price(5, "6654", 1, 2,
                new Date(2013, 0, 17, 0, 0, 0),
                new Date(2013, 0, 22, 0, 0, 0),
                85));

        expectedResult.add(new Price(1, "6654", 1, 2,
                new Date(2013, 0, 10, 0, 0, 0),
                new Date(2013, 0, 17, 0, 0, 0),
                80));
        expectedResult.add(new Price(5, "6654", 1, 2,
                new Date(2013, 0, 17, 0, 0, 0),
                new Date(2013, 0, 22, 0, 0, 0),
                85));
        expectedResult.add(new Price(3, "6654", 1, 2,
                new Date(2013, 0, 22, 0, 0, 0),
                new Date(2013, 0, 25, 0, 0, 0),
                90));

        OperationsWithPriceLists operationsWithPriceLists = new OperationsWithPriceLists();
        Assert.assertEquals(expectedResult, operationsWithPriceLists.mergePriceLists(currentPriceList, importedPriceList));
    }
}