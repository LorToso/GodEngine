package godEngine.gameDependencies;

import godEngine.gameContent.Actor;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;


public class ActorHolder {
    private Vector<Actor> actorList;
    private HashMap<Integer, Actor> actorMap;

    public ActorHolder()
    {
        actorList = new Vector<>();
        actorMap = new HashMap<>();
    }

    public ArrayList<Actor> getObjectsAt(int dx, int dy, Class<? extends Actor> searchedClass)
    {
        ArrayList<Actor> objectsAtPosition = new ArrayList<>();

        for (Actor actor : actorList)
        {
            if(searchedClass.isAssignableFrom(actor.getClass()))
                continue;

            if(actor.getRect().contains(dx, dy))
                objectsAtPosition.add(actor);
        }
        return objectsAtPosition;
    }

    public void addObject(Actor newActor)
    {
        actorList.add(newActor);
        actorMap.put(newActor.getActorID(), newActor);
    }

    public void removeObject(Actor actor)
    {
        actorList.remove(actor);
        actorMap.remove(actor.getActorID());
    }


    public ArrayList<Actor> getObjectsInRect(Rectangle rect, Class<? extends Actor> actorClass)
    {
        ArrayList<Actor> objectsAtPosition = new ArrayList<>();


        for (Actor actor : actorList)
        {
            if(actor.getClass().isAssignableFrom(actorClass))
                continue;

            if(actor.getRect().intersects(rect))
                objectsAtPosition.add(actor);
        }
        return objectsAtPosition;
    }
    public List<Actor> getObjects(Class<? extends Actor> actorClass)
    {
        if(actorClass == Actor.class)
            return actorList;

        ArrayList<Actor> objectsAtPosition = new ArrayList<>();
        for (Actor actor : actorList)
        {
            if(actorClass.isAssignableFrom(actor.getClass()))
                continue;
            objectsAtPosition.add(actor);
        }
        return objectsAtPosition;
    }

    public Actor getById(int actorID) {
        return actorMap.get(actorID);
    }
}
