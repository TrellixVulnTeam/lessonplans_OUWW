package com.enoch.chris.lessonplanwebsite.controller.entity;

import java.time.LocalDate;
import java.util.List;

public class LessonPlan {

	private int id; // database generates id so this field is not required
    private String title; // required
    private LocalDate dateAdded; //required
    private Level level; // required
    private Type type; // required
    private int age; // required  
    private SpeakingAmount speakingAmount; // required  
    private List<Topic> topics;
    private Picture picture;
    
    private LessonTime lessonTime; //default is 60 minutes
    private boolean listening;
    private boolean vocabulary;
    private boolean reading;
    private boolean writing;
    private boolean video;
    private boolean song;
    private boolean funClass;
    private boolean games;
    private boolean jigsaw;
    private boolean translation;
    private short preparationTime; //default is 5 minutes
    private boolean printedMaterialsNeeded;
    
    
    private LessonPlan(LessonPlanBuilder lessonPlanBuilder) {
    	this.id = lessonPlanBuilder.id;
		this.title = lessonPlanBuilder.title;
		this.dateAdded = lessonPlanBuilder.dateAdded;
		this.level = lessonPlanBuilder.level;
		this.type = lessonPlanBuilder.type;
		this.age = lessonPlanBuilder.age;
		this.speakingAmount = lessonPlanBuilder.speakingAmount;
		this.topics =lessonPlanBuilder.topics;
		
		this.lessonTime = lessonPlanBuilder.lessonTime;
		this.picture = lessonPlanBuilder.picture;
		this.listening = lessonPlanBuilder.listening;
		this.vocabulary = lessonPlanBuilder.vocabulary;
		this.reading = lessonPlanBuilder.reading;
		this.writing = lessonPlanBuilder.writing;
		this.video = lessonPlanBuilder.video;
		this.song = lessonPlanBuilder.song;
		this.funClass = lessonPlanBuilder.funClass;
		this.games = lessonPlanBuilder.games;
		this.jigsaw = lessonPlanBuilder.jigsaw;
		this.translation = lessonPlanBuilder.translation;
		this.preparationTime = lessonPlanBuilder.preparationTime;
		this.printedMaterialsNeeded = lessonPlanBuilder.printedMaterialsNeeded;
	}

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDate getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(LocalDate dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public LessonTime getLessonTime() {
		return lessonTime;
	}

	public void setLessonTime(LessonTime lessonTime) {
		this.lessonTime = lessonTime;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public SpeakingAmount getSpeakingAmount() {
		return speakingAmount;
	}

	public void setSpeakingAmount(SpeakingAmount speakingAmount) {
		this.speakingAmount = speakingAmount;
	}

	public boolean isListening() {
		return listening;
	}

	public void setListening(boolean listening) {
		this.listening = listening;
	}

	public boolean isVocabulary() {
		return vocabulary;
	}

	public void setVocabulary(boolean vocabulary) {
		this.vocabulary = vocabulary;
	}

	public boolean isReading() {
		return reading;
	}

	public void setReading(boolean reading) {
		this.reading = reading;
	}

	public boolean isWriting() {
		return writing;
	}

	public void setWriting(boolean writing) {
		this.writing = writing;
	}

	public boolean isVideo() {
		return video;
	}

	public void setVideo(boolean video) {
		this.video = video;
	}

	public boolean isSong() {
		return song;
	}

	public void setSong(boolean song) {
		this.song = song;
	}

	public boolean isFunClass() {
		return funClass;
	}

	public void setFunClass(boolean funClass) {
		this.funClass = funClass;
	}

	public boolean isGames() {
		return games;
	}

	public void setGames(boolean games) {
		this.games = games;
	}

	public boolean isJigsaw() {
		return jigsaw;
	}

	public void setJigsaw(boolean jigsaw) {
		this.jigsaw = jigsaw;
	}

	public boolean isTranslation() {
		return translation;
	}

	public void setTranslation(boolean translation) {
		this.translation = translation;
	}

	public boolean isPrintedMaterialsNeeded() {
		return printedMaterialsNeeded;
	}

	public void setPrintedMaterialsNeeded(boolean printedMaterialsNeeded) {
		this.printedMaterialsNeeded = printedMaterialsNeeded;
	}

	public List<Topic> getTopics() {
		return topics;
	}

	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}

	public Picture getPicture() {
		return picture;
	}

	public void setPicture(Picture picture) {
		this.picture = picture;
	}

	public short getPreparationTime() {
		return preparationTime;
	}

	public void setPreparationTime(short preparationTime) {
		this.preparationTime = preparationTime;
	}







	public static class LessonPlanBuilder {
		private int id; // database automatically generates so id is optional
	    private String title; // required
	    private LocalDate dateAdded; //required
	    private Level level; // required    
	    private Type type; // required
	    private int age; // required  
	    private SpeakingAmount speakingAmount; // required  
		public List<Topic> topics; // required 
	    
		private LessonTime lessonTime = LessonTime.SIXTY;
	    private boolean listening;
	    private boolean vocabulary;
	    private boolean reading;
	    private boolean writing;
	    private boolean video;
	    private boolean song;
	    private boolean funClass;
	    private boolean games;
	    private boolean jigsaw;
	    private boolean translation;
	    private short preparationTime = 5; //5 is default value
	    private boolean printedMaterialsNeeded;
	    private Picture picture;
 
	    
        public LessonPlanBuilder(String title, LocalDate dateAdded,Level level, Type type
        		,int age,SpeakingAmount speakingAmount, List<Topic> topics) {
            this.title = title;
            this.dateAdded = dateAdded;
            this.level = level;
            this.type = type;
            this.age = age;
            this.speakingAmount = speakingAmount;
            this.topics = topics;
        }
                
        public LessonPlanBuilder isListening(boolean isListening) {
            this.listening = isListening;
            return this;
        }
        
        public LessonPlanBuilder isVocabulary(boolean isVocabulary) {
            this.vocabulary = isVocabulary;
            return this;
        }
        public LessonPlanBuilder isReading(boolean isReading) {
            this.reading = isReading;
            return this;
        }
        public LessonPlanBuilder isWriting(boolean isWriting) {
            this.writing = isWriting;
            return this;
        }
        public LessonPlanBuilder isVideo(boolean isVideo) {
            this.video = isVideo;
            return this;
        }
        public LessonPlanBuilder isSong(boolean isSong) {
            this.song = isSong;
            return this;
        }
        public LessonPlanBuilder isFunClass(boolean isFunClass) {
            this.funClass = isFunClass;
            return this;
        }
        
        public LessonPlanBuilder isGames(boolean isGames) {
            this.games = isGames;
            return this;
        }
        
        public LessonPlanBuilder isJigsaw(boolean isJigsaw) {
            this.jigsaw = isJigsaw;
            return this;
        }
        
        public LessonPlanBuilder preparationTime(short preparationTime) {
            this.preparationTime = preparationTime;
            return this;
        }
        
        public LessonPlanBuilder isPrintedMaterialsNeeded(boolean isPrintedMaterialsNeeded) {
            this.printedMaterialsNeeded= isPrintedMaterialsNeeded;
            return this;
        }
        
        public LessonPlanBuilder lessonTime(LessonTime lessonTime) {
            this.lessonTime = lessonTime;
            return this;
        }
        
        public LessonPlanBuilder picture (Picture picture) {
            this.picture = picture;
            return this;
        }
  
        
        //Return the finally constructed LessonPlan object
        public LessonPlan build() {
            LessonPlan lessonPlan =  new LessonPlan(this);
            return lessonPlan;
        }

    }
    
}
