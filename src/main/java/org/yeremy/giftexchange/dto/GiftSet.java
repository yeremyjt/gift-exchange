package org.yeremy.giftexchange.dto;

public class GiftSet
{
    private Person giver;
    private Person receiver;

    public Person getGiver()
    {
        return giver;
    }

    public void setGiver(Person giver)
    {
        this.giver = giver;
    }

    public Person getReceiver()
    {
        return receiver;
    }

    public void setReceiver(Person receiver)
    {
        this.receiver = receiver;
    }
}
