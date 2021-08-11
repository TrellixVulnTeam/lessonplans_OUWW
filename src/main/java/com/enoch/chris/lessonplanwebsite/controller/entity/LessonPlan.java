package com.enoch.chris.lessonplanwebsite.controller.entity;

import java.time.LocalDate;

public class LessonPlan {

	private int id; // database generates id so this field is not required
    private String title; // required
    private LocalDate dateAdded; //required
    private Level level; // required
    private LessonTime lessonTime; // required
    private Type type; // required
    private int age; // required  
    private SpeakingAmount speakingAmount; // required  
    
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
    private short preparationTime;
    private boolean printedMaterialsNeeded;
    
    
    private LessonPlan(LessonPlanBuilder lessonPlanBuilder) {
    	this.id = lessonPlanBuilder.id;
		this.title = lessonPlanBuilder.title;
		this.dateAdded = lessonPlanBuilder.dateAdded;
		this.level = lessonPlanBuilder.level;
		this.lessonTime = lessonPlanBuilder.lessonTime;
		this.type = lessonPlanBuilder.type;
		this.age = lessonPlanBuilder.age;
		this.speakingAmount = lessonPlanBuilder.speakingAmount;
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

	public short isPreparationTime() {
		return preparationTime;
	}

	public void setPreparationTime(short preparationTime) {
		this.preparationTime = preparationTime;
	}

	public boolean isPrintedMaterialsNeeded() {
		return printedMaterialsNeeded;
	}

	public void setPrintedMaterialsNeeded(boolean printedMaterialsNeeded) {
		this.printedMaterialsNeeded = printedMaterialsNeeded;
	}


	public static class LessonPlanBuilder {
		private int id; // database automatically generates so it is optional
	    private String title; // required
	    private LocalDate dateAdded; //required
	    private Level level; // required
	    private LessonTime lessonTime; // required
	    private Type type; // required
	    private int age; // required  
	    private SpeakingAmount speakingAmount; // required  
	    
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
	    private short preparationTime;
	    private boolean printedMaterialsNeeded;
 
	    
        public LessonPlanBuilder(String title, LocalDate dateAdded,Level level,LessonTime lessonTime, Type type
        		,int age,SpeakingAmount speakingAmount) {
            this.title = title;
            this.dateAdded = dateAdded;
            this.level = level;
            this.lessonTime = lessonTime;
            this.type = type;
            this.age = age;
            this.speakingAmount = speakingAmount;
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
        public LessonPlanBuilder isReadinVideo(boolean isVideo) {
            this.video = isVideo;
            return this;
        }
        public LessonPlanBuilder isReadinSong(boolean isSong) {
            this.song = isSong;
            return this;
        }
        public LessonPlanBuilder isFunClass(boolean isFunClass) {
            this.funClass = isFunClass;
            return this;
        }
        
        public LessonPlanBuilder isReadinGames(boolean isGames) {
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
  
        
        //Return the finally constructed LessonPlan object
        public LessonPlan build() {
            LessonPlan lessonPlan =  new LessonPlan(this);
            return lessonPlan;
        }

    }
    
}
