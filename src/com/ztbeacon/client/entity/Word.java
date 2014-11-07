package com.ztbeacon.client.entity;

public class Word {
	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	private String word;
	private String definition;
	public Word(){}
	public Word(String word, String definition) {
		this.word = word;
		this.definition = definition;
	}
}
