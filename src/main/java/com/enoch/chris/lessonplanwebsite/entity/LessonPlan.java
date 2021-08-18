package com.enoch.chris.lessonplanwebsite.entity;

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
	
	//@ManyToOne(fetch = FetchType.LAZY)
	@ManyToOne
	@JoinColumn(name="subscription_id")
    private Subscription assignedSubscription; // required
	
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
    private Boolean listening;
    
	@Column(name="vocabulary")
    private Boolean  vocabulary;
    
	@Column(name="reading")
    private Boolean  reading;
    
	@Column(name="writing")
    private Boolean  writing;
    
	@Column(name="video")
    private Boolean  video;
    
	@Column(name="song")
    private Boolean  song;
    
	@Column(name="fun_class")
    private Boolean  funClass;
    
	@Column(name="games")
    private Boolean  games;
    
	@Column(name="jigsaw")
    private Boolean  jigsaw;
    
	@Column(name="translation")
    private Boolean  translation;
    
	@Column(name="preparation_time")
    private short preparationTime; //default is 5 minutes
    
	@Column(name="printed_materials_needed")
    private Boolean  printedMaterialsNeeded;
    
	@ManyToMany(fetch = FetchType.LAZY,cascade= {CascadeType.DETACH, 
			CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name = "lesson_plan_grammar", 
	joinColumns = @JoinColumn(name = "lesson_plan_id"), 
	inverseJoinColumns = @JoinColumn(name = "grammar_id"))
    private List<Grammar> grammar;
	
	@ManyToMany(fetch = FetchType.LAZY,cascade= {CascadeType.DETACH, 
			CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name = "lesson_plan_tag", 
	joinColumns = @JoinColumn(name = "lesson_plan_id"), 
	inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private List<Tag> tags;
    
 
	
	private LessonPlan() {
		
	}
    
    private LessonPlan(LessonPlanBuilder lessonPlanBuilder) {
    	//Using setters ensures appropriate validation is performed.
    	
    	this.id = lessonPlanBuilder.id;
		this.title = lessonPlanBuilder.title;
		this.dateAdded = lessonPlanBuilder.dateAdded;
		this.assignedSubscription = lessonPlanBuilder.assignedSubscription;
		this.type = lessonPlanBuilder.type;
		this.age = lessonPlanBuilder.age;
		this.speakingAmount = lessonPlanBuilder.speakingAmount;
		this.topics =lessonPlanBuilder.topics;
		this.tags = lessonPlanBuilder.tags;
		
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

	public Boolean  isListening() {
		return listening;
	}

	public void setListening(Boolean  listening) {
		this.listening = listening;
	}

	public Boolean  isVocabulary() {
		return vocabulary;
	}

	public void setVocabulary(Boolean  vocabulary) {
		this.vocabulary = vocabulary;
	}

	public Boolean  isReading() {
		return reading;
	}

	public void setReading(Boolean  reading) {
		this.reading = reading;
	}

	public Boolean  isWriting() {
		return writing;
	}

	public void setWriting(Boolean  writing) {
		this.writing = writing;
	}

	public Boolean  isVideo() {
		return video;
	}

	public void setVideo(Boolean  video) {
		this.video = video;
	}

	public Boolean  isSong() {
		return song;
	}

	public void setSong(Boolean  song) {
		this.song = song;
	}

	public Boolean  isFunClass() {
		return funClass;
	}

	public void setFunClass(Boolean  funClass) {
		this.funClass = funClass;
	}

	public Boolean  isGames() {
		return games;
	}

	public void setGames(Boolean  games) {
		this.games = games;
	}

	public Boolean  isJigsaw() {
		return jigsaw;
	}

	public void setJigsaw(Boolean  jigsaw) {
		this.jigsaw = jigsaw;
	}

	public Boolean  isTranslation() {
		return translation;
	}

	public void setTranslation(Boolean  translation) {
		this.translation = translation;
	}

	public Boolean  isPrintedMaterialsNeeded() {
		return printedMaterialsNeeded;
	}

	public void setPrintedMaterialsNeeded(Boolean  printedMaterialsNeeded) {
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

	public Subscription getAssignedSubscription() {
		return assignedSubscription;
	}

	public void setAssignedSubscription(Subscription assignedSubscription) {
		this.assignedSubscription = assignedSubscription;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public static class LessonPlanBuilder {
		
		private int id; // database automatically generates so id is optional
	    private String title; // required
	    private LocalDate dateAdded; //required
	    private Subscription assignedSubscription; // required    
	    private Type type; // required
	    private int age; // required  
	    private SpeakingAmount speakingAmount; // required  
		public List<Topic> topics; // required 
		public List<Tag> tags; //required
	    
		private LessonTime lessonTime = LessonTime.SIXTY;
	    private Boolean  listening;
	    private Boolean  vocabulary;
	    private Boolean  reading;
	    private Boolean  writing;
	    private Boolean  video;
	    private Boolean  song;
	    private Boolean  funClass;
	    private Boolean  games;
	    private Boolean  jigsaw;
	    private Boolean  translation;
	    private short preparationTime = 5;
	    private Boolean  printedMaterialsNeeded;
	    private Picture picture;
	    private List<Grammar> grammar;
 
	    
        public LessonPlanBuilder(String title, LocalDate dateAdded,Subscription assignedSubscription, Type type
        		,int age,SpeakingAmount speakingAmount, List<Topic> topics, List<Tag> tags) {
            this.title = title;
            this.dateAdded = dateAdded;
            this.assignedSubscription = assignedSubscription;
            this.type = type;
            this.age = age;
            this.speakingAmount = speakingAmount;
            this.topics = topics;
            this.tags = tags;
        }
                
        public LessonPlanBuilder isListening(Boolean  isListening) {
            this.listening = isListening;
            return this;
        }
        
        public LessonPlanBuilder isVocabulary(Boolean  isVocabulary) {
            this.vocabulary = isVocabulary;
            return this;
        }
        public LessonPlanBuilder isReading(Boolean  isReading) {
            this.reading = isReading;
            return this;
        }
        public LessonPlanBuilder isWriting(Boolean  isWriting) {
            this.writing = isWriting;
            return this;
        }
        public LessonPlanBuilder isVideo(Boolean  isVideo) {
            this.video = isVideo;
            return this;
        }
        public LessonPlanBuilder isSong(Boolean  isSong) {
            this.song = isSong;
            return this;
        }
        public LessonPlanBuilder isFunClass(Boolean  isFunClass) {
            this.funClass = isFunClass;
            return this;
        }
        
        public LessonPlanBuilder isGames(Boolean  isGames) {
            this.games = isGames;
            return this;
        }
        
        public LessonPlanBuilder isJigsaw(Boolean  isJigsaw) {
            this.jigsaw = isJigsaw;
            return this;
        }
        
        public LessonPlanBuilder preparationTime(short preparationTime) {
            this.preparationTime = preparationTime;
            return this;
        }
        
        public LessonPlanBuilder isPrintedMaterialsNeeded(Boolean  isPrintedMaterialsNeeded) {
            this.printedMaterialsNeeded= isPrintedMaterialsNeeded;
            return this;
        }
        
        public LessonPlanBuilder isTranslation(Boolean  isTranslation) {
            this.translation= isTranslation;
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
