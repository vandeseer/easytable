package com.aquent.rambo.easytable;

import com.aquent.rambo.easytable.drawing.Drawer;
import com.aquent.rambo.easytable.drawing.DrawingContext;
import com.aquent.rambo.easytable.drawing.cell.AbstractCellDrawer;
import com.aquent.rambo.easytable.structure.Table;
import com.aquent.rambo.easytable.structure.cell.AbstractCell;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class TableWithinTableCellDrawer {
	@Getter
	@SuperBuilder
	public static class TableWithinTableCell extends AbstractCell {

		private Table table;

		@Override
		public float getMinHeight() {
			return table.getHeight() + getVerticalPadding();
		}

		@Override
		protected Drawer createDefaultDrawer() {
			return new TableWithinTableDrawer(this);
		}

		public class TableWithinTableDrawer extends AbstractCellDrawer<TableWithinTableCell> {

			public TableWithinTableDrawer(TableWithinTableCell tableWithinTableCell) {
				this.cell = tableWithinTableCell;
			}

			@Override
			public void drawContent(DrawingContext drawingContext) {
				TableDrawer.builder().startX(drawingContext.getStartingPoint().x + getPaddingLeft())
						.startY(drawingContext.getStartingPoint().y + cell.getHeight() - getPaddingTop())
						.table(this.cell.getTable()).contentStream(drawingContext.getContentStream()).build().draw();
			}

			@Override
			protected float calculateInnerHeight() {
				return cell.table.getHeight();
			}
		}

	}

}
