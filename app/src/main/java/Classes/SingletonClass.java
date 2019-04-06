package Classes;

import Activities.Favourites;

public class SingletonClass
{
    private static SingletonClass singletonObject;

    private Favourites m_favourite;

    private SingletonClass()
    {
        //   Optional Code
    }

    public static synchronized SingletonClass getSingletonObject()
    {
        if (singletonObject == null)
        {
            singletonObject = new SingletonClass();
        }
        return singletonObject;
    }

    public void clear()
    {
        singletonObject = null;
    }

    public Object clone() throws CloneNotSupportedException
    {
        throw new CloneNotSupportedException();
    }

    public Favourites getFavourite()
    {
        return m_favourite;
    }

    public void setFavourite(Favourites m_favourite)
    {
        this.m_favourite = m_favourite;
    }
}