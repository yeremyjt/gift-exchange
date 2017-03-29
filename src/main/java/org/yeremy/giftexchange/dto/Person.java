package org.yeremy.giftexchange.dto;

import org.springframework.core.style.ToStringCreator;

public class Person
{
    private int id;
    private String name;
    private PersonType type;
    private String familyName;
    private String familyGroupName;

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

    public String getFamilyGroupName()
    {
        return familyGroupName;
    }

    public void setFamilyGroupName(String familyGroup)
    {
        this.familyGroupName = familyGroup;
    }

    @Override
    public String toString()
    {
        return this.name;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }

        if (obj instanceof Person)
        {
            Person that = (Person) obj;
            if (that.getId() == this.id && that.getName().equals(this.name) && that.getType() == this.type
                    && that.familyName.equals(this.familyName) && that.familyGroupName.equals(this.familyGroupName))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
}
