package picturegallery.persistency;

/*-
 * BEGIN-LICENSE
 * picturegallery
 * %%
 * Copyright (C) 2016 - 2022 Johannes Meier
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * END-LICENSE
 */

import gallery.RealPicture;
import javafx.scene.Node;

public interface MediaRenderBase {
	public Node getShownNode();

	public interface PictureProvider {
		public RealPicture get();
	}
	public void renderPicture(PictureProvider provider);
	public void renderPicture(RealPicture picture);


	public final static double detailRationXDefault = 0.5;
	public final static double detailRationYDefault = 0.5;
	public final static double zoomDefault = 1.0;

	void setZoom(double newZoom);
	void setDetailX(double newX);
	void setDetailY(double newY);

	double getCurrentImageHeight();
	double getCurrentImageWidth();
	double getCurrentNodeHeight();
	double getCurrentNodeWidth();
}
