package Classes;

import Activities.Favourites;

public class CommonModelClass
{
    private static CommonModelClass singletonObject;

    private Favourites baseActivity;

    private CommonModelClass()
    {
        //   Optional Code
    }

    public static synchronized CommonModelClass getSingletonObject()
    {
        if (singletonObject == null)
        {
            singletonObject = new CommonModelClass();
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

    public Favourites getbaseActivity()
    {
        return baseActivity;
    }

    public void setbaseActivity(Favourites baseActivity)
    {
        this.baseActivity = baseActivity;
    }
}