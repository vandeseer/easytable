package com.aquent.rambo.easytable.structure.cell;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AnnotationCommentToTableData {
	private int currentCnt;
	private boolean isReminderCovered;
}
