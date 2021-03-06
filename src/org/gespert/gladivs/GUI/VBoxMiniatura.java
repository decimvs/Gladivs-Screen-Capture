/*
 * Copyright (C) 2016 - 2018 Guillermo Espert Carrasquer [gespert@yahoo.es]
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.gespert.gladivs.GUI;

import javafx.scene.layout.VBox;
import org.gespert.gladivs.Screenshots.MonitorData;

/**
 *
 * @author gespe
 */
public class VBoxMiniatura extends VBox {
    
    protected boolean Selected;
    protected MonitorData monitorData;
    
    public VBoxMiniatura(MonitorData md){
        super();
        
        Selected = false;
        monitorData = monitorData;
    }
    
    public VBoxMiniatura(double spacing, MonitorData md){
        super(spacing);
        
        Selected = false;
        monitorData = md;
    }
    
    public void setSelected(boolean s){
        Selected = s;
    }
    
    public boolean getSelected(){
        return Selected;
    }
    
    public MonitorData getMonitorData(){
        return monitorData;
    }
}