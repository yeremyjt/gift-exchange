package org.yeremy.giftexchange.domain;

public class Person
{
    private int id;
    private String name;
    private PersonType type;
    private String familyName;
    private String familyGroup;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public PersonType getType()
    {
        return type;
    }

    public void setType(PersonType type)
    {
        this.type = type;
    }

    public String getFamilyName()
    {
        return familyName;
    }

    public void setFamilyName(String familyName)
    {
        this.familyName = familyName;
    }

    public String getFamilyGroup()
    {
        return familyGroup;
    }

    public void setFamilyGroup(String familyGroup)
    {
        this.familyGroup = familyGroup;
    }
}
