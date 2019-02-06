package com.msst.platform.facade;

import com.msst.platform.service.dto.StartTranslateSubtitleTranslateInfo;

public interface SubtitleFacade {
  void startTranslateSubtitle(StartTranslateSubtitleTranslateInfo subtitle, String movieId);
}
