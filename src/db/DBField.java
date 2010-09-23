/* $Id$ */

package db;

public class DBField
{
	/*
	 * Назва поля таблиці БД
	 */
	public String key;
	
	/*
	 * Заголовок стовпця
	 */
	public String caption;
	
	/*
	 * Тип комірки таблиці JTable
	 */
	public Class<?> type;
//	public Class<?extends CellDefault> type;
	
	/*
	 * Значення за замовчуванням
	 */
	public Object defaultValue;
	
	/*
	 * Прапорець, що вказує чи зміни значення поля мають записуватися в таблицю БД
	 */
	public boolean flagUpdate = true;
	
	public DBField(String key, String caption, Class<?> type, Object defaultValue, boolean flagUpdate)
	{
		this.caption = caption;
		this.key = key;
		this.type = type;
		this.defaultValue = defaultValue;
		this.flagUpdate = flagUpdate;
	}

	public DBField(String key, String caption, Class<?> type, Object defaultValue)
	{
		this.caption = caption;
		this.key = key;
		this.type = type;
		this.defaultValue = defaultValue;
	}

	public DBField(String key, String caption, Class<?> type)
	{
		this.caption = caption;
		this.key = key;
		this.type = type;
		this.defaultValue = null;
	}	

	public DBField(String key, String caption)
	{
		this.caption = caption;
		this.key = key;
		this.type = String.class;
		this.defaultValue = null;
		this.flagUpdate = false;
	}
}
