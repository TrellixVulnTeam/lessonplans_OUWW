package com.enoch.chris.lessonplanwebsite.controller.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name="LessonPlan")
@Table(name="lesson_plan")
public class LessonPlan {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id; // database generates id so this field is not required
	
	@Column(name="title")
    private String title; // required
	
	@Column(name="dateAdded")
    private LocalDate dateAdded; //required
	
	@Column(name="level")
    private Level level; // required
	
	@Column(name="type")
    private Type type; // required
	
	@Column(name="age")
    private int age; // required  
	
	@Column(name="speaking_amount")
    private SpeakingAmount speakingAmount; // required  
	
	@ManyToMany(fetch = FetchType.LAZY,cascade= {CascadeType.DETACH, 
			CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name = "lesson_plan_topic", 
	joinColumns = @JoinColumn(name = "lesson_plan_id"), 
	inverseJoinColumns = @JoinColumn(name = "topic_id"))
    private List<Topic> topics;
	
	@ManyToOne(fetch = FetchType.LAZY)
    private Picture picture;
    
	@Column(name="lesson_time")
    private LessonTime lessonTime; //default is 60 minutes
	
	@Column(name="listening")
    private boolean listening;
    
	@Column(name="vocabulary")
    private boolean vocabulary;
    
	@Column(name="reading")
    private boolean reading;
    
	@Column(name="writing")
    private boolean writing;
    
	@Column(name="video")
    private boolean video;
    
	@Column(name="song")
    private boolean song;
    
	@Column(name="fun_class")
    private boolean funClass;
    
	@Column(name="games")
    private boolean games;
    
	@Column(name="jigsaw")
    private boolean jigsaw;
    
	@Column(name="translation")
    private boolean translation;
    
	@Column(name="preparation_time")
    private short preparationTime; //default is 5 minutes
    
	@Column(name="printed_materials_needed")
    private boolean printedMaterialsNeeded;
    
	@OneToMany(cascade= {CascadeType.DETACH, 
			CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name = "id")
    private List<Grammar> grammar;
    
    
    
    private LessonPlan(LessonPlanBuilder lessonPlanBuilder) {
    	//Using setters ensures appropriate validation and synchronisation is performed in LessonPlan class. For instance, when a picture is added to LessonPlan, setPicture ensures that LessonPlan is in turn added to Picture. 
    	
    	this.setId(lessonPlanBuilder.id);
    	this.setTitle(lessonPlanBuilder.title);	
		this.setDateAdded(lessonPlanBuilder.dateAdded);
		this.setLevel(lessonPlanBuilder.level);
		this.setType( lessonPlanBuilder.type);
		this.setAge(lessonPlanBuilder.age);
		this.setSpeakingAmount(lessonPlanBuilder.speakingAmount);
		this.setTopics(lessonPlanBuilder.topics);

		this.setLessonTime(lessonPlanBuilder.lessonTime);	
		this.setPicture(lessonPlanBuilder.picture);
		this.setListening(lessonPlanBuilder.listening);
		this.setVocabulary(lessonPlanBuilder.vocabulary);
		this.setReading(lessonPlanBuilder.reading);
		this.setWriting(lessonPlanBuilder.writing);
		this.setVideo(lessonPlanBuilder.video);
		this.setSong(lessonPlanBuilder.song);
		this.setFunClass(lessonPlanBuilder.funClass);
		this.setGames(lessonPlanBuilder.games);
		this.setJigsaw(lessonPlanBuilder.jigsaw);
		this.setTranslation(lessonPlanBuilder.translation);
		this.setPreparationTime(lessonPlanBuilder.preparationTime);
		this.setPrintedMaterialsNeeded(lessonPlanBuilder.printedMaterialsNeeded);
		this.setGrammar(lessonPlanBuilder.grammar);

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

	/**
	 * gets the picture associated with this lesson plan. Do not use this method to remove a picture from the Lesson Plan object.
	 * Use {@link com.enoch.chris.entity.LessonPlan#removePicture} so that both the picture and the LessonPlan objects remain correctly 
	 * synchronised.
	 * @return
	 */
	public Picture getPicture() {
		return picture;
	}

	public void setPicture(Picture picture) {
		this.picture = picture;
		picture.addLessonPlan(this);
	}
	
	public void removePicture(Picture picture) {
		picture.removeLessonPlan(this);
		this.picture = null;		
	}
	

	public short getPreparationTime() {
		return preparationTime;
	}

	public void setPreparationTime(short preparationTime) {
		this.preparationTime = preparationTime;
	}

	public List<Grammar> getGrammar() {
		return grammar;
	}

	public void setGrammar(List<Grammar> grammar) {
		this.grammar = grammar;
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
	    private List<Grammar> grammar;
 
	    
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
        
        public LessonPlanBuilder grammar (List<Grammar> grammar) {
            this.grammar = grammar;
            return this;
        }
  
        
        //Return the finally constructed LessonPlan object
        public LessonPlan build() {
            LessonPlan lessonPlan =  new LessonPlan(this);
            return lessonPlan;
        }

    }
    
}
