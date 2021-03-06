/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.access.to.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import static java.util.Objects.requireNonNull;
import java.util.Set;
import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.youbai.opentcs.access.to.CreationTO;
import org.youbai.opentcs.data.model.Block;
import org.youbai.opentcs.data.model.Color;
/**
 * A transfer object describing a block in the plant model.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public class BlockCreationTO
    extends CreationTO
    implements Serializable {
  /**
   * This block's type.
   */
  @Nonnull@JsonProperty("name")
  private Block.Type type = Block.Type.SINGLE_VEHICLE_ONLY;
  /**
   * This block's member names.
   */
  @JsonProperty("memberNames")
  private Set<String> memberNames = new HashSet<>();
  /**
   * The information regarding the grahical representation of this block.
   */
  @JsonProperty("layout")
  private Layout layout = new Layout();

  /**
   * Creates a new instance.
   *
   * @param name The name of this block.
   */
  public BlockCreationTO(String name) {
    super(name);
  }

  /**
   * Creates a new block.
   *
   * @param name the name of the new block.
   * @param memberNames the names of the block's members.
   * @param properties the properties.
   */
  @JsonCreator
  public BlockCreationTO(@Nonnull String name,
                         @JsonProperty("properties")@Nonnull Map<String, String> properties,
                         @JsonProperty("type")@Nonnull Block.Type type,
                         @JsonProperty("memberNames")@Nonnull Set<String> memberNames,
                         @JsonProperty("layout")@Nonnull Layout layout) {
    super(name,properties);
    this.type = type;
    this.memberNames = memberNames;
    this.layout = layout;
  }
  /**
   * Creates a copy of this object with the given name.
   *
   * @param name The new name.
   * @return A copy of this object, differing in the given name.
   */
  @Override
  public BlockCreationTO withName(@Nonnull String name) {
    return new BlockCreationTO(name,
                               getModifiableProperties(),
                               type,
                               memberNames,
                               layout);
  }

  /**
   * Creates a copy of this object with the given properties.
   *
   * @param properties The new properties.
   * @return A copy of this object, differing in the given properties.
   */
  @Override
  public BlockCreationTO withProperties(@Nonnull Map<String, String> properties) {
    return new BlockCreationTO(getName(),
                               properties,
                               type,
                               memberNames,
                               layout);
  }

  /**
   * Creates a copy of this object and adds the given property.
   * If value == null, then the key-value pair is removed from the properties.
   *
   * @param key the key.
   * @param value the value
   * @return A copy of this object that either
   * includes the given entry in it's current properties, if value != null or
   * excludes the entry otherwise.
   */
  @Override
  public BlockCreationTO withProperty(@Nonnull String key, @Nonnull String value) {
    return new BlockCreationTO(getName(),
                               propertiesWith(key, value),
                               type,
                               memberNames,
                               layout);
  }

  /**
   * Returns the type of this block.
   *
   * @return The type of this block.
   */
  @Nonnull
  public Block.Type getType() {
    return type;
  }

  /**
   * Creates a copy of this object with the given type.
   *
   * @param type The new type.
   * @return A copy of this object, differing in the given type.
   */
  public BlockCreationTO withType(@Nonnull Block.Type type) {
    return new BlockCreationTO(getName(),
                               getModifiableProperties(),
                               type,
                               memberNames,
                               layout);
  }

  /**
   * Returns the names of this block's members.
   *
   * @return The names of this block's members.
   */
  @Nonnull
  public Set<String> getMemberNames() {
    return Collections.unmodifiableSet(memberNames);
  }

  /**
   * Creates a copy of this object with the given members.
   *
   * @param memberNames The names of the block's members.
   * @return A copy of this object, differing in the given value.
   */
  public BlockCreationTO withMemberNames(@Nonnull Set<String> memberNames) {
    return new BlockCreationTO(getName(),
                               getModifiableProperties(),
                               type,
                               memberNames,
                               layout);
  }

  /**
   * Returns the information regarding the grahical representation of this block.
   *
   * @return The information regarding the grahical representation of this block.
   */
  public Layout getLayout() {
    return layout;
  }

  /**
   * Creates a copy of this object, with the given layout.
   *
   * @param layout The value to be set in the copy.
   * @return A copy of this object, differing in the given value.
   */
  public BlockCreationTO withLayout(Layout layout) {
    return new BlockCreationTO(getName(),
                               getModifiableProperties(),
                               type,
                               memberNames,
                               layout);
  }

  public void setType(@Nonnull Block.Type type) {
    this.type = type;
  }

  public void setMemberNames(@Nonnull Set<String> memberNames) {
    this.memberNames = memberNames;
  }

  public void setLayout(Layout layout) {
    this.layout = layout;
  }

  @Override
  public String toString() {
    return "BlockCreationTO{"
        + "name=" + getName()
        + ", type=" + type
        + ", memberNames=" + memberNames
        + ", layout=" + layout
        + ", properties=" + getProperties()
        + '}';
  }

  /**
   * Contains information regarding the grahical representation of a block.
   */
  public static class Layout
      implements Serializable {

    /**
     * The color in which block elements are to be emphasized.
     */
    private final Color color;

    /**
     * Creates a new instance.
     */
    public Layout() {
      this(Color.RED);
    }

    /**
     * Creates a new instance.
     *
     * @param color The color in which block elements are to be emphasized.
     */
    @JsonCreator
    public Layout(@JsonProperty("color") Color color) {
      this.color = requireNonNull(color, "color");
    }

    /**
     * Returns the color in which block elements are to be emphasized.
     *
     * @return The color in which block elements are to be emphasized.
     */
    public Color getColor() {
      return color;
    }

    /**
     * Creates a copy of this object, with the given color.
     *
     * @param color The value to be set in the copy.
     * @return A copy of this object, differing in the given value.
     */
    public Layout withColor(Color color) {
      return new Layout(color);
    }

    @Override
    public String toString() {
      return "Layout{"
          + "color=" + color
          + '}';
    }
  }
}
