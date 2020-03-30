package com.aquent.rambo.easytable.structure.cell;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class ReviewComment {
	private String userDisplayName;
	private String reviewCommentUserDisplayName;
	private String postedOn;
	private String comments;
	private Integer commentNumber;
	private ReviewComment reviewComment;
	private Set<ReviewComment> reviewComments = new HashSet<>(0);
	
}
