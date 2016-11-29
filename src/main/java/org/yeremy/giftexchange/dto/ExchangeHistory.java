package org.yeremy.giftexchange.dto;

public class ExchangeHistory
{
    private int id;
    private int giverId;
    private String giverName;
    private int receiverId;
    private String receiverName;
    private String familyGroup;
    private int year;

    public String getFamilyGroup()
    {
        return familyGroup;
    }

    public void setFamilyGroup(String familyGroup)
    {
        this.familyGroup = familyGroup;
    }

    public String getGiverName()
    {
        return giverName;
    }

    public void setGiverName(String giverName)
    {
        this.giverName = giverName;
    }

    public String getReceiverName()
    {
        return receiverName;
    }

    public void setReceiverName(String receiverName)
    {
        this.receiverName = receiverName;
    }



    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getGiverId()
    {
        return giverId;
    }

    public void setGiverId(int giverId)
    {
        this.giverId = giverId;
    }

    public int getReceiverId()
    {
        return receiverId;
    }

    public void setReceiverId(int receiverId)
    {
        this.receiverId = receiverId;
    }

    public int getYear()
    {
        return year;
    }

    public void setYear(int year)
    {
        this.year = year;
    }
}
