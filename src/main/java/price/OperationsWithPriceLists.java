package price;

import java.util.*;

/**
 * Класс предоставляющий оперции для работы с ценами
 */
public class OperationsWithPriceLists {
    /**
     * Метод объединения имеющихся цен с вновь импортированными из внешней системы
     * @param currentPriceList имеющиеся цены
     * @param importedPriceList импортированные цены
     * @return результат объединения цен
     */
    public List<Price> mergePriceLists(List<Price> currentPriceList, List<Price> importedPriceList) {
        ListIterator<Price> importedPriceListIterator = importedPriceList.listIterator();
        LinkedList<Price> resultPriceList = new LinkedList<>();

        while(importedPriceListIterator.hasNext()) {
            Price importedPrice = importedPriceListIterator.next();
            ListIterator<Price> currentPriceListIterator = currentPriceList.listIterator();

            while(currentPriceListIterator.hasNext()) {
                Price currentPrice = currentPriceListIterator.next();
                /* одинаковый продукт, департамент и номер */
                if(importedPrice.productCode.equals(currentPrice.productCode)
                        && importedPrice.number == currentPrice.number
                        && importedPrice.depart == currentPrice.depart) {
                    /* периоды пересекаются */
                    if(importedPrice.isIntersects(currentPrice)) {
                        /* цены совпадают */
                        if(importedPrice.value == currentPrice.value) {
                            unionPricesWithSameValue(importedPriceListIterator, resultPriceList, importedPrice, currentPriceListIterator, currentPrice);
                        }
                        /* цены не совпадают*/
                        else {
                            unionPricesWithoutSamePrices(importedPriceListIterator, resultPriceList, importedPrice, currentPriceListIterator, currentPrice);
                        }
                    }
                }
            }
        }

        resultPriceList.addAll(importedPriceList);
        resultPriceList.addAll(currentPriceList);
        return resultPriceList;
    }

    /**
     * Объединение цен с разными значениями цен
     * @param importedPriceListIterator итератор списка импортируемых цен
     * @param resultPriceList результирущий список цен
     * @param importedPrice импортируемая цена
     * @param currentPriceListIterator итератор списка текущих цен
     * @param currentPrice текущая цена
     */
    private void unionPricesWithoutSamePrices(ListIterator<Price> importedPriceListIterator, LinkedList<Price> resultPriceList, Price importedPrice, ListIterator<Price> currentPriceListIterator, Price currentPrice) {
        /* диапазон импортируемой цены оканчивается до диапазона текущей цены */
        if(importedPrice.before(currentPrice)) {
            Price editedPrice = new Price(currentPrice);
            editedPrice.begin = importedPrice.end;
            currentPriceListIterator.set(editedPrice);
            resultPriceList.add(importedPrice);
            importedPriceListIterator.remove();
        }
        /* диапазон импортируемой цены оканчивается после диапазона текущей цены */
        else if(importedPrice.after(currentPrice)) {
            Price editedPrice = new Price(currentPrice);
            editedPrice.end = importedPrice.begin;
            resultPriceList.add(editedPrice);
            currentPriceListIterator.remove();
        }
         /* диапазон импортируемой цены внутри диапазона текущей цены */
        else if(importedPrice.consistIn(currentPrice)){
            Price resultPrice = new Price(currentPrice);
            resultPrice.setEnd(importedPrice.begin);
            resultPriceList.add(resultPrice);
            resultPriceList.add(importedPrice);
            resultPrice = new Price(currentPrice);
            resultPrice.setBegin(importedPrice.end);
            resultPriceList.add(resultPrice);
            currentPriceListIterator.remove();
            importedPriceListIterator.remove();
        }
    }

    /**
     * Объединение цен с одинаковыми значениями цен
     * @param importedPriceListIterator итератор списка импортируемых цен
     * @param resultPriceList результирущий список цен
     * @param importedPrice импортируемая цена
     * @param currentPriceListIterator итератор списка текущих цен
     * @param currentPrice текущая цена
     */
    private void unionPricesWithSameValue(ListIterator<Price> importedPriceListIterator, LinkedList<Price> resultPriceList, Price importedPrice, ListIterator<Price> currentPriceListIterator, Price currentPrice) {
        if(currentPrice.before(importedPrice)) {
            importedPriceListIterator.set(unionPrices(currentPrice, importedPrice));
        }
        else {
            resultPriceList.add(unionPrices(currentPrice, importedPrice));
            importedPriceListIterator.remove();
        }
        currentPriceListIterator.remove();
    }

    /**
     * Объединение одинаковых цен по периоду (цена одинаковая, периоды пересекаются)
     * @param firstPrice первая цена
     * @param secondPrice вторая цена
     * @return новая цена с объединенным периодом
     */
    private Price unionPrices(Price firstPrice, Price secondPrice){
        Price result = new Price(firstPrice.id, firstPrice.productCode, firstPrice.number, firstPrice.depart, firstPrice.value);

        if(firstPrice.begin.before(secondPrice.begin) || firstPrice.begin.equals(secondPrice.begin))
            result.setBegin(firstPrice.begin);
        else
            result.setBegin(secondPrice.begin);

        if(firstPrice.end.before(secondPrice.end) || firstPrice.end.equals(secondPrice.end))
            result.setEnd(secondPrice.end);
        else
            result.setEnd(firstPrice.end);

        return result;
    }

}
