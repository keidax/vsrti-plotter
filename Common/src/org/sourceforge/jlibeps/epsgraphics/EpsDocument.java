/*
 * Copyright 2001-2006 Paul James Mutton, http://www.jibble.org/ Copyright 2007
 * Arnaud Blouin This file is part of jlibeps. jlibeps is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version. jlibeps is distributed
 * without any warranty; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.<br>
 */

package org.sourceforge.jlibeps.epsgraphics;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

/**
 * This represents an EPS document. Several EpsGraphics2D objects may point to
 * the same EpsDocument.<br>
 * Copyright 2001-2006 Paul James Mutton, http://www.jibble.org/<br>
 * Copyright 2007 Arnaud Blouin<br>
 * 08/09/07
 * 
 * @version 0.1
 */
public class EpsDocument {
    
    private float minX;
    
    private float minY;
    
    private float maxX;
    
    private float maxY;
    
    private boolean _isClipSet = false;
    
    private String _title;
    
    private StringWriter _stringWriter;
    
    private BufferedWriter _bufferedWriter = null;
    
    // We need to remember which was the last EpsGraphics2D object to use
    // us, as we need to replace the clipping region if another EpsGraphics2D
    // object tries to use us.
    private EpsGraphics2D _lastG = null;
    
    /**
     * Constructs an empty EpsDevice.
     * 
     * @since 0.1
     */
    public EpsDocument(String title) {
        _title = title;
        minX = Float.POSITIVE_INFINITY;
        minY = Float.POSITIVE_INFINITY;
        maxX = Float.NEGATIVE_INFINITY;
        maxY = Float.NEGATIVE_INFINITY;
        _stringWriter = new StringWriter();
        _bufferedWriter = new BufferedWriter(_stringWriter);
    }
    
    /**
     * Constructs an empty EpsDevice that writes directly to a file. Bounds must
     * be set before use.
     * 
     * @since 0.1
     */
    public EpsDocument(String title, OutputStream outputStream, int minX, int minY, int maxX, int maxY) throws IOException {
        _title = title;
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        _bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
        write(_bufferedWriter);
    }
    
    /**
     * Returns the title of the EPS document.
     * 
     * @since 0.1
     */
    public synchronized String getTitle() {
        return _title;
    }
    
    /**
     * Updates the bounds of the current EPS document.
     * 
     * @since 0.1
     */
    public synchronized void updateBounds(double x, double y) {
        if (x > maxX) {
            maxX = (float) x;
        }
        if (x < minX) {
            minX = (float) x;
        }
        if (y > maxY) {
            maxY = (float) y;
        }
        if (y < minY) {
            minY = (float) y;
        }
    }
    
    /**
     * Appends a line to the EpsDocument. A new line character is added to the
     * end of the line when it is added.
     * 
     * @since 0.1
     */
    public synchronized void append(EpsGraphics2D g, String line) {
        if (_lastG == null) {
            _lastG = g;
        } else if (g != _lastG) {
            EpsGraphics2D lastG = _lastG;
            _lastG = g;
            
            // We are being drawn on with a different EpsGraphics2D context.
            // We may need to update the clip, etc from this new context.
            if (g.getClip() != lastG.getClip()) {
                g.setClip(g.getClip());
            }
            
            if (!g.getColor().equals(lastG.getColor())) {
                g.setColor(g.getColor());
            }
            
            if (!g.getBackground().equals(lastG.getBackground())) {
                g.setBackground(g.getBackground());
            }
            
            // We don't need this, as this only affects the stroke and font,
            // which are dealt with separately later on.
            // if (!g.getTransform().equals(lastG.getTransform())) {
            // g.setTransform(g.getTransform());
            // }
            if (!g.getPaint().equals(lastG.getPaint())) {
                g.setPaint(g.getPaint());
            }
            
            if (!g.getComposite().equals(lastG.getComposite())) {
                g.setComposite(g.getComposite());
            }
            
            if (!g.getComposite().equals(lastG.getComposite())) {
                g.setComposite(g.getComposite());
            }
            
            if (!g.getFont().equals(lastG.getFont())) {
                g.setFont(g.getFont());
            }
            
            if (!g.getStroke().equals(lastG.getStroke())) {
                g.setStroke(g.getStroke());
            }
        }
        
        _lastG = g;
        
        try {
            _bufferedWriter.write(line + "\n");
        } catch (IOException e) {
            throw new EpsException("Could not write to the output file: " + e);
        }
    }
    
    /**
     * Outputs the contents of the EPS document to the specified Writer,
     * complete with headers and bounding box.
     * 
     * @since 0.1
     */
    public synchronized void write(Writer writer) throws IOException {
        float offsetX = -minX;
        float offsetY = -minY;
        
        writer.write("%!PS-Adobe-3.0 EPSF-3.0\n");
        writer.write("%%Creator: jlibeps " + EpsGraphics2D.VERSION + ", https://sourceforge.net/projects/jlibeps/" + "\n");
        writer.write("%%Title: " + _title + "\n");
        writer.write("%%CreationDate: " + new Date() + "\n");
        writer.write("%%BoundingBox: 0 0 " + (int) Math.ceil(maxX + offsetX) + " " + (int) Math.ceil(maxY + offsetY) + "\n");
        writer.write("%%DocumentData: Clean7Bit\n");
        writer.write("%%DocumentProcessColors: Black\n");
        writer.write("%%ColorUsage: Color\n");
        writer.write("%%Origin: 0 0\n");
        writer.write("%%Pages: 1\n");
        writer.write("%%Page: 1 1\n");
        writer.write("%%EndComments\n\n");
        
        writer.write("gsave\n");
        
        if (_stringWriter != null) {
            writer.write(offsetX + " " + offsetY + " translate\n");
            
            _bufferedWriter.flush();
            StringBuffer buffer = _stringWriter.getBuffer();
            
            for (int i = 0; i < buffer.length(); i++) {
                writer.write(buffer.charAt(i));
            }
            
            writeFooter(writer);
        } else {
            writer.write(offsetX + " " + (maxY - minY - offsetY) + " translate\n");
        }
        
        writer.flush();
    }
    
    private void writeFooter(Writer writer) throws IOException {
        writer.write("grestore\n");
        
        if (isClipSet()) {
            writer.write("grestore\n");
        }
        
        writer.write("showpage\n");
        writer.write("\n");
        writer.write("%%EOF");
        writer.flush();
    }
    
    public synchronized void flush() throws IOException {
        _bufferedWriter.flush();
    }
    
    public synchronized void close() throws IOException {
        if (_stringWriter == null) {
            writeFooter(_bufferedWriter);
            _bufferedWriter.flush();
            _bufferedWriter.close();
        }
    }
    
    public boolean isClipSet() {
        return _isClipSet;
    }
    
    public void setClipSet(boolean isClipSet) {
        _isClipSet = isClipSet;
    }
}
