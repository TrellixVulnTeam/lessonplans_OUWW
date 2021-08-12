package com.enoch.chris.lessonplanwebsite.controller.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
	@Enumerated(EnumType.STRING)
    private Level level; // required
	
	@Column(name="type")
	@Enumerated(EnumType.STRING)
    private Type type; // required
	
	@Column(name="age")
    private int age; // required  
	
	@Column(name="speaking_amount")
	@Enumerated(EnumType.STRING)
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
	@Enumerated(EnumType.STRING)
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
    
	@ManyToMany(fetch = FetchType.LAZY,cascade= {CascadeType.DETACH, 
			CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name = "lesson_plan_grammar", 
	joinColumns = @JoinColumn(name = "lesson_plan_id"), 
	inverseJoinColumns = @JoinColumn(name = "grammar_id"))
    private List<Grammar> grammar;
    
    
    
    private LessonPlan(LessonPlanBuilder lessonPlanBuilder) {
    	//Using setters ensures appropriate validation is performed.
    	
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
		this.grammar = lessonPlanBuilder.grammar;
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
	
	

	@Override
	public String toString() {
		return "LessonPlan [id=" + id + ", title=" + title + ", dateAdded=" + dateAdded + ", level=" + level + ", type="
				+ type + ", age=" + age + ", speakingAmount=" + speakingAmount + ", topics=" + topics + ", picture="
				+ picture + ", lessonTime=" + lessonTime + ", listening=" + listening + ", vocabulary=" + vocabulary
				+ ", reading=" + reading + ", writing=" + writing + ", video=" + video + ", song=" + song
				+ ", funClass=" + funClass + ", games=" + games + ", jigsaw=" + jigsaw + ", translation=" + translation
				+ ", preparationTime=" + preparationTime + ", printedMaterialsNeeded=" + printedMaterialsNeeded
				+ ", grammar=" + grammar + ", getId()=" + getId() + ", getTitle()=" + getTitle() + ", getDateAdded()="
				+ getDateAdded() + ", getLevel()=" + getLevel() + ", getLessonTime()=" + getLessonTime()
				+ ", getType()=" + getType() + ", getAge()=" + getAge() + ", getSpeakingAmount()=" + getSpeakingAmount()
				+ ", isListening()=" + isListening() + ", isVocabulary()=" + isVocabulary() + ", isReading()="
				+ isReading() + ", isWriting()=" + isWriting() + ", isVideo()=" + isVideo() + ", isSong()=" + isSong()
				+ ", isFunClass()=" + isFunClass() + ", isGames()=" + isGames() + ", isJigsaw()=" + isJigsaw()
				+ ", isTranslation()=" + isTranslation() + ", isPrintedMaterialsNeeded()=" + isPrintedMaterialsNeeded()
				+ ", getTopics()=" + getTopics() + ", getPicture()=" + getPicture() + ", getPreparationTime()="
				+ getPreparationTime() + ", getGrammar()=" + getGrammar() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
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
	    
		private LessonTime lessonTime; // = LessonTime.SIXTY;
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
	    private short preparationTime; // = 5; //5 is default value
	    private boolean printedMaterialsNeeded;
	    private Picture picture;
	    private List<Grammar> grammar;
 
	    
	    public LessonPlanBuilder(String title, LocalDate dateAdded
        		,int age, List<Topic> topics) {
            this.title = title;
            this.dateAdded = dateAdded;
            this.age = age;
            this.topics = topics;
        }
	    
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
        
        public LessonPlanBuilder topics (List<Topic> topics) {
            this.topics = topics;
            return this;
        }
  
        
        //Return the finally constructed LessonPlan object
        public LessonPlan build() {
            LessonPlan lessonPlan =  new LessonPlan(this);
            synchroniseLessonPlanAndPicture(lessonPlan);
            return lessonPlan;
        }
        
        private void synchroniseLessonPlanAndPicture(LessonPlan lessonPlan) {
        	Picture picture = lessonPlan.getPicture();
        	if (picture != null) {
        		picture.addLessonPlan(lessonPlan);
        	}        	 
        	
        }

    }
    
}
