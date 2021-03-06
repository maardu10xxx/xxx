/*
   Copyright 2008 Simon Mieth

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package org.kabeja.svg.generators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.kabeja.dxf.DXFAttrib;
import org.kabeja.dxf.DXFBlock;
import org.kabeja.dxf.DXFColor;
import org.kabeja.dxf.DXFEntity;
import org.kabeja.dxf.DXFInsert;
import org.kabeja.dxf.helpers.Point;
import org.kabeja.math.TransformContext;
import org.kabeja.svg.SVGConstants;
import org.kabeja.svg.SVGContext;
import org.kabeja.svg.SVGGenerationException;
import org.kabeja.svg.SVGSAXGenerator;
import org.kabeja.svg.SVGSAXGeneratorManager;
import org.kabeja.svg.SVGUtils;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;


public class SVGInsertGenerator extends AbstractSVGSAXGenerator {
    //protected SVGSAXGeneratorManager manager;

    public void toSAX(ContentHandler handler, Map svgContext, DXFEntity entity,
        TransformContext transformContext) throws SAXException {
        DXFInsert insert = (DXFInsert) entity;
        if (insert.getSectionName()==null || !insert.getSectionName().equals("entity") ){
        	return;
        }

        //System.out.println(insert.getSectionName()+":"+insert.getBlockID()+":"+((DXFAttrib)insert.attribs.get(0)).getAttribTag()+":"+((DXFAttrib)insert.attribs.get(0)).getText());
        DXFBlock block = insert.getDXFDocument().getDXFBlock(insert.getBlockID());

        StringBuffer buf = new StringBuffer();
        
        Point referencePoint = block.getReferencePoint();
        
         int rows = insert.getRows();
        int columns = insert.getColumns();
        double rotate = insert.getRotate();
        Point insertPoint = insert.getPoint();
        double scale_x = insert.getScaleX();
        double scale_y = insert.getScaleY();
        double column_spacing = insert.getColumnSpacing();
        double row_spacing = insert.getRowSpacing();

        ArrayList list = insert.attribs;


       // try {
           // SVGSAXGenerator gen = this.manager.getSVGGenerator("attrib");

        AttributesImpl attrX = new AttributesImpl();
        boolean f_loaded = false;
        //entity内部的解析使用loaded/partnumber选项,如果loaded/partnumber不存在的均不显示
            Iterator i = list.iterator();
            //每个attribute的内部遍历?
        	//f_loaded = true;
            while (i.hasNext()) {
                DXFAttrib attrib = (DXFAttrib) i.next();
                //parse the component location
                if (attrib.getAttribTag().toUpperCase().equals("REFNAME") ){
                	if(!attrib.getText().isEmpty()){
                	SVGUtils.addAttribute(attrX, SVGConstants.XML_ID,
                        attrib.getText());
                	//f_loaded = true;
                       // gen.toSAX(handler, svgContext, attrib, null);
                	}
                }else if (attrib.getAttribTag().toUpperCase().equals("TECHNOLOGY")){
                	if(!attrib.getText().isEmpty()){
                    	SVGUtils.addAttribute(attrX, SVGConstants.SVG_ATTRIBUTE_TECHNOLOGY,
                            attrib.getText());
                	}

                }else if (attrib.getAttribTag().toUpperCase().equals("LOADED")){
                	if(!attrib.getText().isEmpty()){
                    	SVGUtils.addAttribute(attrX, SVGConstants.SVG_ATTRIBUTE_LOADED,
                            attrib.getText());
                    	f_loaded = true;
                 	}

                }else if (attrib.getAttribTag().toUpperCase().equals("PART") ){
                	if(!attrib.getText().isEmpty()){
                    	SVGUtils.addAttribute(attrX, SVGConstants.SVG_ATTRIBUTE_PARTNUMBER2,
                            attrib.getText());
                	}
                	f_loaded = true;
                }else if (attrib.getAttribTag().toUpperCase().equals("PARTNUMBER") ){
                	if(!attrib.getText().isEmpty()){
                    	SVGUtils.addAttribute(attrX, SVGConstants.SVG_ATTRIBUTE_PARTNUMBER1,
                            attrib.getText());
                	}
                	f_loaded = true;
                }else if (attrib.getAttribTag().toUpperCase().equals("$$DEVICE$$") ){
                	if(!attrib.getText().isEmpty()){
                    	SVGUtils.addAttribute(attrX, SVGConstants.SVG_ATTRIBUTE_DEVICE,
                            attrib.getText());
                	}
                	f_loaded = true;
                }
            }
        	

        if (f_loaded == true){
        	//if (insert.getSectionName().equals("entity")){
                SVGUtils.startElement(handler, SVGConstants.SVG_GROUP, attrX);
        	//}
                /*  } catch (SVGGenerationException e) {
                e.printStackTrace();
                }*/
                //SVGUtils.textDocumentToSAX(handler, text.getTextDocument());
                

                // translate to the insert point all the rows and columns
                for (int column = 0; column < columns; column++) {
                    for (int row = 0; row < rows; row++) {
                        // translate to the insert point
                        buf.append("translate(");
                        buf.append(SVGUtils.formatNumberAttribute((insertPoint.getX() -
                                (column_spacing * column))));
                        buf.append(SVGConstants.SVG_ATTRIBUTE_PATH_PLACEHOLDER);
                        buf.append(SVGUtils.formatNumberAttribute((insertPoint.getY() -
                                (row_spacing * row))));
                        buf.append(")");

                        // then rotate
                        if (rotate != 0) {
                            buf.append(" rotate(");
                            buf.append(SVGUtils.formatNumberAttribute(rotate));
                            buf.append(")");
                        }

                        // then scale
                        if ((scale_x != 1.0) || (scale_y != 1.0)) {
                            buf.append(" scale(");
                            buf.append(SVGUtils.formatNumberAttribute(scale_x));
                            buf.append(SVGConstants.SVG_ATTRIBUTE_PATH_PLACEHOLDER);
                            buf.append(SVGUtils.formatNumberAttribute(scale_y));
                            buf.append(")");
                        }

                        if ((referencePoint.getX() != 0.0) ||
                                (referencePoint.getY() != 0.0)) {
                            buf.append(" translate(");
                            buf.append(SVGUtils.formatNumberAttribute(
                                    (-1 * referencePoint.getX())));
                            buf.append(SVGConstants.SVG_ATTRIBUTE_PATH_PLACEHOLDER);
                            buf.append(SVGUtils.formatNumberAttribute(
                                    (-1 * referencePoint.getY())));
                            buf.append(")");
                        }

                        AttributesImpl attr = new AttributesImpl();
                        SVGUtils.addAttribute(attr, "transform", buf.toString());

                        // add common attributes
                        super.setCommonAttributes(attr, svgContext, insert);

                        // fix the scale of stroke-width
                        if (((scale_x + scale_y) != 0.0) &&
                                svgContext.containsKey(SVGContext.LAYER_STROKE_WIDTH)) {
                            Double lw = (Double) svgContext.get(SVGContext.LAYER_STROKE_WIDTH);
                            double width = (lw.doubleValue() * 2) / (scale_x + scale_y);
                            SVGUtils.addAttribute(attr,
                                SVGConstants.SVG_ATTRIBUTE_STROKE_WITDH,
                                SVGUtils.formatNumberAttribute(width));
                        }

                        // SVGUtils.startElement(handler, SVGConstants.SVG_GROUP, attr);
                        // attr = new AttributesImpl();
                        attr.addAttribute(SVGConstants.XMLNS_NAMESPACE, "xlink",
                            "xmlns:xlink", "CDATA", SVGConstants.XLINK_NAMESPACE);

                        attr.addAttribute(SVGConstants.XLINK_NAMESPACE, "href",
                            "xlink:href", "CDATA",
                            "#" + SVGUtils.validateID(insert.getBlockID()));

                        SVGUtils.emptyElement(handler, SVGConstants.SVG_USE, attr);

                    	if (insert.getSectionName().equals("entity")){
                            SVGUtils.endElement(handler, SVGConstants.SVG_GROUP);
                    	}
                        buf.delete(0, buf.length());
                    }
                }
            }
        
    }
}
