export interface ITranslateInfo {
  parentLineId?: string;
  currentLineId?: string;
  sequence?: string;
  parentText?: string;
}

export const defaultValue: Readonly<ITranslateInfo> = {};
