package org.vandeseer.easytable.structure.cell;

import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.json.JSONObject;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class AnnotationComment {

	JSONObject markUpJson;
	Integer markupTypeId;
	private PDImageXObject highlightedAreaImage;
	String commentNumber;
	String stampBackgroundColor;
	String stampLabelText;
	String stampLabelColor;
}
