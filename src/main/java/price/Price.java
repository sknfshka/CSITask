package price;

import java.util.Date;

/**
 * Класс цены
 */
public class Price {
    protected long id;                // идентификатор в БД
    protected String productCode;     // код товара
    protected int number;             // номер цены
    protected int depart;             // номер отдела
    protected Date begin;             // начало действия
    protected Date end;               // конец действия
    protected long value;             // значение цены в копейках

    /**
     * Конструктор по умолчанию
     */
    public Price() {

    }

    /**
     * Копирующий конструктор
     */
    public Price(Price price) {
        this.id = price.id;
        this.productCode = price.productCode;
        this.number = price.number;
        this.depart = price.depart;
        this.begin = price.begin;
        this.end = price.end;
        this.value = price.value;
    }

    /**
     * Конструктор для инициализации полей класса
     */
    public Price(long id, String productCode, int number, int depart, Date begin, Date end, long value) {
        this.id = id;
        this.productCode = productCode;
        this.number = number;
        this.depart = depart;
        this.begin = begin;
        this.end = end;
        this.value = value;
    }

    /**
     * Конструктор для инициализации полей класса, кроме дат
     */
    public Price(long id, String productCode, int number, int depart, long value) {
        this.id = id;
        this.productCode = productCode;
        this.number = number;
        this.depart = depart;
        this.value = value;
    }

    /**
     * Пересечение периодов
     * @param price цена, с периодом которого происходит сравнение
     * @return true - периоды пересекаются, false - не пересекаются
     */
    public boolean isIntersects(Price price) {
        return (this.begin.before(price.end)) && (price.begin.before(this.end));
    }

    /**
     * Период цены идет до price
     * @param price цена, с периодом которого происходит сравнение
     * @return true - период текущей цены идет до price
     */
    public boolean before(Price price) {
        return (this.begin.before(price.begin)) && (price.begin.before(this.end));
    }

    /**
     * Период цены идет после price
     * @param price цена, с периодом которого происходит сравнение
     * @return true - период текущей цены идет после price
     */
    public boolean after(Price price) {
        return (this.begin.before(price.end)) && (price.end.before(this.end));
    }

    /**
     * Период цены расположен в пределах периода price
     * @param price цена, с периодом которого происходит сравнение
     * @return true - период текущей цены расположен в пределах диапазона цены price
     */
    public boolean consistIn(Price price) {
        return this.begin.after(price.begin) && this.end.before(price.end);
    }

    @Override
    public String toString() {
        return "" + this.productCode + ", " + this.number + ", " +  this.depart+ ", " +
                this.begin.toString() + ", " +  this.end.toString() + ", " +  this.value;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return  true;

        if(obj == null || getClass() != obj.getClass())
            return false;

        Price price = (Price)obj;

        return this.id == price.id &&
                this.productCode.equals(price.productCode) &&
                this.number == price.number &&
                this.depart == price.depart &&
                this.begin.equals(price.begin) &&
                this.end.equals(price.end) &&
                this.value == price.value;
    }

}
