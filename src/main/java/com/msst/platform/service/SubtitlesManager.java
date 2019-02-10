package com.msst.platform.service;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.msst.platform.domain.file.locator.SubtitleContent;
import com.msst.platform.domain.file.locator.google.GoogleDriverLocator;

public class SubtitlesManager {
	private GoogleDriverLocator locator;

	@Autowired
	public SubtitlesManager(GoogleDriverLocator locator) {
		this.locator = locator;
	}

	public void saveAllSubtitlesOnLocalMachine() {
		List<SubtitleContent> subtitles = locator.getAllSubtitles();
		subtitles.forEach(el -> {
			saveSubtitle(el);
		});
	}
	
	public void saveSubtitlesByMovieName(String movieName) {
		List<SubtitleContent> subtitles = locator.getSubtitlesByMovieName(movieName);
		subtitles.forEach(el -> {
			saveSubtitle(el);
		});
	}

	private void saveSubtitle(SubtitleContent src) {
		try (Writer writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream("localFiles\\" + src.getSubtitleName() + ".srt"), "utf-8"))) {
			writer.write(src.getFileContent());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
