/*
 * Copyright (C) 2012 Tim Vaughan <tgvaughan@gmail.com>
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
package hamlet;

import com.google.common.collect.Lists;
import java.util.List;

/**
 * Model additionally containing inheritance reaction groups.
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class InheritanceModel extends Model {
    
    List<InheritanceReactionGroup> inheritanceReactionGroups;
    
    /**
     * InheritanceModel constructor.
     */
    public InheritanceModel() {
        super();
        inheritanceReactionGroups = Lists.newArrayList();
    }
    
    public void addInheritanceReactionGroup(InheritanceReactionGroup irGroup) {
        addReactionGroup(irGroup);
        inheritanceReactionGroups.add(irGroup);
    }
}
