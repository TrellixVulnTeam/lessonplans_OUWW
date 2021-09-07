package com.enoch.chris.lessonplanwebsite.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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

import org.springframework.data.annotation.CreatedDate;

@Entity(name="LessonPlan")
@Table(name="lesson_plan")
public class LessonPlan {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id; // database generates id so this field is not required
	
	@Column(name="title")
    private String title; // required
	
	@CreatedDate
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
    private Set<Topic> topics;
	
	@ManyToOne
    private Picture picture;
    
	@Column(name="lesson_time")
	@Enumerated(EnumType.STRING)
    private LessonTime lessonTime; //default is 60 minutes
	
	@Column(name="listening")
    private boolean listening;
    
	@Column(name="vocabulary")
    private boolean  vocabulary;
    
	@Column(name="reading")
    private boolean  reading;
    
	@Column(name="writing")
    private boolean  writing;
    
	@Column(name="video")
    private boolean  video;
    
	@Column(name="song")
    private boolean  song;
    
	@Column(name="fun_class")
    private boolean  funClass;
    
	@Column(name="games")
    private boolean  games;
    
	@Column(name="jigsaw")
    private boolean  jigsaw;
    
	@Column(name="translation")
    private boolean  translation;
    
	@Column(name="preparation_time")
	@Enumerated(EnumType.STRING)
    private PreparationTime preparationTime; 
    
	@Column(name="no_printed_materials_needed")
    private boolean  noPrintedMaterialsNeeded;
    
	@ManyToMany(fetch = FetchType.LAZY,cascade= {CascadeType.DETACH, 
			CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name = "lesson_plan_grammar", 
	joinColumns = @JoinColumn(name = "lesson_plan_id"), 
	inverseJoinColumns = @JoinColumn(name = "grammar_id"))
    private Set<Grammar> grammar;
	
	@ManyToMany(fetch = FetchType.LAZY,cascade= {CascadeType.DETACH, 
			CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name = "lesson_plan_tag", 
	joinColumns = @JoinColumn(name = "lesson_plan_id"), 
	inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private Set<Tag> tags;
    
 
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LessonPlan other = (LessonPlan) obj;
		if (id != other.id)
			return false;
		return true;
	}



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
		this.noPrintedMaterialsNeeded = lessonPlanBuilder.noPrintedMaterialsNeeded;
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

	public boolean  getListening() {
		return listening;
	}

	public void setListening(boolean  listening) {
		this.listening = listening;
	}

	
	public boolean  getVocabulary() {
		return vocabulary;
	}
	
	

	public void setVocabulary(boolean  vocabulary) {
		this.vocabulary = vocabulary;
	}

	public boolean  getReading() {
		return reading;
	}

	public void setReading(boolean  reading) {
		this.reading = reading;
	}

	public boolean  getWriting() {
		return writing;
	}

	public void setWriting(boolean  writing) {
		this.writing = writing;
	}

	public boolean  getVideo() {
		return video;
	}

	public void setVideo(boolean  video) {
		this.video = video;
	}

	public boolean  getSong() {
		return song;
	}

	public void setSong(boolean  song) {
		this.song = song;
	}

	public boolean  getFunClass() {
		return funClass;
	}

	public void setFunClass(boolean  funClass) {
		this.funClass = funClass;
	}

	public boolean  getGames() {
		return games;
	}

	public void setGames(boolean  games) {
		this.games = games;
	}

	public boolean  getJigsaw() {
		return jigsaw;
	}

	public void setJigsaw(boolean  jigsaw) {
		this.jigsaw = jigsaw;
	}

	public boolean  getTranslation() {
		return translation;
	}

	public void setTranslation(boolean  translation) {
		this.translation = translation;
	}

	public boolean  getNoPrintedMaterialsNeeded() {
		return noPrintedMaterialsNeeded;
	}

	public void setNoPrintedMaterialsNeeded(boolean  noPrintedMaterialsNeeded) {
		this.noPrintedMaterialsNeeded = noPrintedMaterialsNeeded;
	}

	public Set<Topic> getTopics() {
		return topics;
	}

	public void setTopics(Set<Topic> topics) {
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
	

	public PreparationTime getPreparationTime() {
		return preparationTime;
	}

	public void setPreparationTime(PreparationTime preparationTime) {
		this.preparationTime = preparationTime;
	}

	public Set<Grammar> getGrammar() {
		return grammar;
	}

	public void setGrammar(Set<Grammar> grammar) {
		this.grammar = grammar;
	}

	public Subscription getAssignedSubscription() {
		return assignedSubscription;
	}

	public void setAssignedSubscription(Subscription assignedSubscription) {
		this.assignedSubscription = assignedSubscription;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}
	
	

	@Override
	public String toString() {
		return "LessonPlan [id=" + id + ", title=" + title + ", dateAdded=" + dateAdded + ", assignedSubscription="
				+ assignedSubscription + ", type=" + type + ", age=" + age + ", speakingAmount=" + speakingAmount
				+ ", topics=" + topics + ", picture=" + picture + ", lessonTime=" + lessonTime + ", listening="
				+ listening + ", vocabulary=" + vocabulary + ", reading=" + reading + ", writing=" + writing
				+ ", video=" + video + ", song=" + song + ", funClass=" + funClass + ", games=" + games + ", jigsaw="
				+ jigsaw + ", translation=" + translation + ", preparationTime=" + preparationTime
				+ ", noPrintedMaterialsNeeded=" + noPrintedMaterialsNeeded + ", grammar=" + grammar + ", tags=" + tags
				+ "]";
	}

	public static class LessonPlanBuilder {
		
		private int id; // database automatically generates so id is optional
	    private String title; // required
	    private LocalDate dateAdded; //required
	    private Subscription assignedSubscription; // required    
	    private Type type; // required
	    private int age; // required  
	    private SpeakingAmount speakingAmount; // required  
		public Set<Topic> topics; // required 
		public Set<Tag> tags; //required
	    
		private LessonTime lessonTime = LessonTime.SIXTY;
	    private boolean  listening;
	    private boolean  vocabulary;
	    private boolean  reading;
	    private boolean  writing;
	    private boolean  video;
	    private boolean  song;
	    private boolean  funClass;
	    private boolean  games;
	    private boolean  jigsaw;
	    private boolean  translation;
	    private PreparationTime preparationTime = PreparationTime.FIVE;
	    private boolean  noPrintedMaterialsNeeded;
	    private Picture picture;
	    private Set<Grammar> grammar;
 
	    
        public LessonPlanBuilder(String title, LocalDate dateAdded,Subscription assignedSubscription, Type type
        		,int age,SpeakingAmount speakingAmount, Set<Topic> topics, Set<Tag> tags) {
            this.title = title;
            this.dateAdded = dateAdded;
            this.assignedSubscription = assignedSubscription;
            this.type = type;
            this.age = age;
            this.speakingAmount = speakingAmount;
            this.topics = topics;
            this.tags = tags;
        }
                
        public LessonPlanBuilder isListening(boolean  isListening) {
            this.listening = isListening;
            return this;
        }
        
        public LessonPlanBuilder isVocabulary(boolean  isVocabulary) {
            this.vocabulary = isVocabulary;
            return this;
        }
        public LessonPlanBuilder isReading(boolean  isReading) {
            this.reading = isReading;
            return this;
        }
        public LessonPlanBuilder isWriting(boolean  isWriting) {
            this.writing = isWriting;
            return this;
        }
        public LessonPlanBuilder isVideo(boolean  isVideo) {
            this.video = isVideo;
            return this;
        }
        public LessonPlanBuilder isSong(boolean  isSong) {
            this.song = isSong;
            return this;
        }
        public LessonPlanBuilder isFunClass(boolean  isFunClass) {
            this.funClass = isFunClass;
            return this;
        }
        
        public LessonPlanBuilder isGames(boolean  isGames) {
            this.games = isGames;
            return this;
        }
        
        public LessonPlanBuilder isJigsaw(boolean  isJigsaw) {
            this.jigsaw = isJigsaw;
            return this;
        }
        
        public LessonPlanBuilder preparationTime(PreparationTime preparationTime) {
            this.preparationTime = preparationTime;
            return this;
        }
        
        public LessonPlanBuilder isNoPrintedMaterialsNeeded(boolean  isNoPrintedMaterialsNeeded) {
            this.noPrintedMaterialsNeeded= isNoPrintedMaterialsNeeded;
            return this;
        }
        
        public LessonPlanBuilder isTranslation(boolean  isTranslation) {
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
        
        public LessonPlanBuilder grammar (Set<Grammar> grammar) {
            this.grammar = grammar;
            return this;
        }
        
        public LessonPlanBuilder topics (Set<Topic> topics) {
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
