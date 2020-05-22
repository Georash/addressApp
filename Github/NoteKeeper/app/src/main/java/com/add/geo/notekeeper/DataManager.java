package com.add.geo.notekeeper;

import java.util.ArrayList;
import java.util.List;

//this is a singleton class that means it occurs once in the app
public class DataManager {

    private static DataManager ourInstance = null ;

    private List<CourseInfo> mCourses = new ArrayList<>();
    private List<NoteInfo> mNotes = new ArrayList<>();


    public static DataManager getInstance() {

        if (ourInstance == null) {
            ourInstance = new DataManager();
            ourInstance.initializeCourses();
            ourInstance.initializeExampleNotes();
        }

        return ourInstance;
    }


    public String getCurrentUsername(){

        return "George Addison";
    }

    public String getCurrentEmail(){

        return "rockyveller@gmail.com";
    }

    public List<NoteInfo> getNotes() {

        return mNotes;
    }

    //this is where we create a new note
    public int createNote(){

        NoteInfo note = new NoteInfo(null,null,null);
        mNotes.add(note);
        return mNotes.size() - 1 ;
    }


    //we find the note here
    public int findNote(NoteInfo note) {

        for (int index = 0 ; index < mNotes.size(); index++){
            if(note.equals(mNotes.get(index)))
                return index;
        }

        return -1 ;
    }

    public void removeNote(int index) {

        mNotes.remove(index);
    }

    //returns a list of all courses
    public List<CourseInfo> getCourses() {

        return mCourses;
    }
    public CourseInfo getCourse(String id){

        for(CourseInfo course: mCourses){
            if(id.equals(course.getmCourseId()))
                return course;
        }

        return null;
    }

}
