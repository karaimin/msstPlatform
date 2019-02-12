package com.msst.platform.facade;

import com.msst.platform.domain.LineVersion;
import com.msst.platform.domain.Subtitle;
import com.msst.platform.service.dto.StartTranslateSubtitleTranslateInfo;
import com.msst.platform.service.dto.TranslatingLineInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SubtitleFacade {
  void startTranslateSubtitle(StartTranslateSubtitleTranslateInfo subtitle, String movieId);

  Mono<Subtitle> getPendingSubtitle(String id);

  Flux<TranslatingLineInfo> getParentLinesInfo(String subtitleId);

  List<TranslatingLineInfo> getParentLinesInfoList(String subtitleId);

  List<LineVersion> getTranslatedLineVersions(String lineId);

  LineVersion addNewTranslatedLine(String id, LineVersion lineVersion);

  void finishSubtitleTranslate(String subtitleId);

  List<Subtitle> getSubtitlesFinishedTranslation();
}
