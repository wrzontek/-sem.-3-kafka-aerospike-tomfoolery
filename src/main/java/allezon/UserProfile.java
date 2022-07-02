/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package allezon;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class UserProfile extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 260240887039957598L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"UserProfile\",\"namespace\":\"allezon\",\"fields\":[{\"name\":\"cookie\",\"type\":\"string\"},{\"name\":\"buys\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"UserTag\",\"fields\":[{\"name\":\"time\",\"type\":\"string\"},{\"name\":\"cookie\",\"type\":\"string\"},{\"name\":\"country\",\"type\":\"string\"},{\"name\":\"device\",\"type\":\"string\"},{\"name\":\"action\",\"type\":\"string\"},{\"name\":\"origin\",\"type\":\"string\"},{\"name\":\"product_info\",\"type\":{\"type\":\"record\",\"name\":\"ProductInfo\",\"fields\":[{\"name\":\"product_id\",\"type\":\"string\"},{\"name\":\"brand_id\",\"type\":\"string\"},{\"name\":\"category_id\",\"type\":\"string\"},{\"name\":\"price\",\"type\":\"int\"}]}}]}}},{\"name\":\"views\",\"type\":{\"type\":\"array\",\"items\":\"UserTag\"}}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<UserProfile> ENCODER =
      new BinaryMessageEncoder<UserProfile>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<UserProfile> DECODER =
      new BinaryMessageDecoder<UserProfile>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<UserProfile> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<UserProfile> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<UserProfile> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<UserProfile>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this UserProfile to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a UserProfile from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a UserProfile instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static UserProfile fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  @Deprecated public java.lang.CharSequence cookie;
  @Deprecated public java.util.List<allezon.UserTag> buys;
  @Deprecated public java.util.List<allezon.UserTag> views;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public UserProfile() {}

  /**
   * All-args constructor.
   * @param cookie The new value for cookie
   * @param buys The new value for buys
   * @param views The new value for views
   */
  public UserProfile(java.lang.CharSequence cookie, java.util.List<allezon.UserTag> buys, java.util.List<allezon.UserTag> views) {
    this.cookie = cookie;
    this.buys = buys;
    this.views = views;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return cookie;
    case 1: return buys;
    case 2: return views;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: cookie = (java.lang.CharSequence)value$; break;
    case 1: buys = (java.util.List<allezon.UserTag>)value$; break;
    case 2: views = (java.util.List<allezon.UserTag>)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'cookie' field.
   * @return The value of the 'cookie' field.
   */
  public java.lang.CharSequence getCookie() {
    return cookie;
  }


  /**
   * Sets the value of the 'cookie' field.
   * @param value the value to set.
   */
  public void setCookie(java.lang.CharSequence value) {
    this.cookie = value;
  }

  /**
   * Gets the value of the 'buys' field.
   * @return The value of the 'buys' field.
   */
  public java.util.List<allezon.UserTag> getBuys() {
    return buys;
  }


  /**
   * Sets the value of the 'buys' field.
   * @param value the value to set.
   */
  public void setBuys(java.util.List<allezon.UserTag> value) {
    this.buys = value;
  }

  /**
   * Gets the value of the 'views' field.
   * @return The value of the 'views' field.
   */
  public java.util.List<allezon.UserTag> getViews() {
    return views;
  }


  /**
   * Sets the value of the 'views' field.
   * @param value the value to set.
   */
  public void setViews(java.util.List<allezon.UserTag> value) {
    this.views = value;
  }

  /**
   * Creates a new UserProfile RecordBuilder.
   * @return A new UserProfile RecordBuilder
   */
  public static allezon.UserProfile.Builder newBuilder() {
    return new allezon.UserProfile.Builder();
  }

  /**
   * Creates a new UserProfile RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new UserProfile RecordBuilder
   */
  public static allezon.UserProfile.Builder newBuilder(allezon.UserProfile.Builder other) {
    if (other == null) {
      return new allezon.UserProfile.Builder();
    } else {
      return new allezon.UserProfile.Builder(other);
    }
  }

  /**
   * Creates a new UserProfile RecordBuilder by copying an existing UserProfile instance.
   * @param other The existing instance to copy.
   * @return A new UserProfile RecordBuilder
   */
  public static allezon.UserProfile.Builder newBuilder(allezon.UserProfile other) {
    if (other == null) {
      return new allezon.UserProfile.Builder();
    } else {
      return new allezon.UserProfile.Builder(other);
    }
  }

  /**
   * RecordBuilder for UserProfile instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<UserProfile>
    implements org.apache.avro.data.RecordBuilder<UserProfile> {

    private java.lang.CharSequence cookie;
    private java.util.List<allezon.UserTag> buys;
    private java.util.List<allezon.UserTag> views;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(allezon.UserProfile.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.cookie)) {
        this.cookie = data().deepCopy(fields()[0].schema(), other.cookie);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.buys)) {
        this.buys = data().deepCopy(fields()[1].schema(), other.buys);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.views)) {
        this.views = data().deepCopy(fields()[2].schema(), other.views);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
    }

    /**
     * Creates a Builder by copying an existing UserProfile instance
     * @param other The existing instance to copy.
     */
    private Builder(allezon.UserProfile other) {
      super(SCHEMA$);
      if (isValidValue(fields()[0], other.cookie)) {
        this.cookie = data().deepCopy(fields()[0].schema(), other.cookie);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.buys)) {
        this.buys = data().deepCopy(fields()[1].schema(), other.buys);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.views)) {
        this.views = data().deepCopy(fields()[2].schema(), other.views);
        fieldSetFlags()[2] = true;
      }
    }

    /**
      * Gets the value of the 'cookie' field.
      * @return The value.
      */
    public java.lang.CharSequence getCookie() {
      return cookie;
    }


    /**
      * Sets the value of the 'cookie' field.
      * @param value The value of 'cookie'.
      * @return This builder.
      */
    public allezon.UserProfile.Builder setCookie(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.cookie = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'cookie' field has been set.
      * @return True if the 'cookie' field has been set, false otherwise.
      */
    public boolean hasCookie() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'cookie' field.
      * @return This builder.
      */
    public allezon.UserProfile.Builder clearCookie() {
      cookie = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'buys' field.
      * @return The value.
      */
    public java.util.List<allezon.UserTag> getBuys() {
      return buys;
    }


    /**
      * Sets the value of the 'buys' field.
      * @param value The value of 'buys'.
      * @return This builder.
      */
    public allezon.UserProfile.Builder setBuys(java.util.List<allezon.UserTag> value) {
      validate(fields()[1], value);
      this.buys = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'buys' field has been set.
      * @return True if the 'buys' field has been set, false otherwise.
      */
    public boolean hasBuys() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'buys' field.
      * @return This builder.
      */
    public allezon.UserProfile.Builder clearBuys() {
      buys = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'views' field.
      * @return The value.
      */
    public java.util.List<allezon.UserTag> getViews() {
      return views;
    }


    /**
      * Sets the value of the 'views' field.
      * @param value The value of 'views'.
      * @return This builder.
      */
    public allezon.UserProfile.Builder setViews(java.util.List<allezon.UserTag> value) {
      validate(fields()[2], value);
      this.views = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'views' field has been set.
      * @return True if the 'views' field has been set, false otherwise.
      */
    public boolean hasViews() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'views' field.
      * @return This builder.
      */
    public allezon.UserProfile.Builder clearViews() {
      views = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public UserProfile build() {
      try {
        UserProfile record = new UserProfile();
        record.cookie = fieldSetFlags()[0] ? this.cookie : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.buys = fieldSetFlags()[1] ? this.buys : (java.util.List<allezon.UserTag>) defaultValue(fields()[1]);
        record.views = fieldSetFlags()[2] ? this.views : (java.util.List<allezon.UserTag>) defaultValue(fields()[2]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<UserProfile>
    WRITER$ = (org.apache.avro.io.DatumWriter<UserProfile>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<UserProfile>
    READER$ = (org.apache.avro.io.DatumReader<UserProfile>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeString(this.cookie);

    long size0 = this.buys.size();
    out.writeArrayStart();
    out.setItemCount(size0);
    long actualSize0 = 0;
    for (allezon.UserTag e0: this.buys) {
      actualSize0++;
      out.startItem();
      e0.customEncode(out);
    }
    out.writeArrayEnd();
    if (actualSize0 != size0)
      throw new java.util.ConcurrentModificationException("Array-size written was " + size0 + ", but element count was " + actualSize0 + ".");

    long size1 = this.views.size();
    out.writeArrayStart();
    out.setItemCount(size1);
    long actualSize1 = 0;
    for (allezon.UserTag e1: this.views) {
      actualSize1++;
      out.startItem();
      e1.customEncode(out);
    }
    out.writeArrayEnd();
    if (actualSize1 != size1)
      throw new java.util.ConcurrentModificationException("Array-size written was " + size1 + ", but element count was " + actualSize1 + ".");

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.cookie = in.readString(this.cookie instanceof Utf8 ? (Utf8)this.cookie : null);

      long size0 = in.readArrayStart();
      java.util.List<allezon.UserTag> a0 = this.buys;
      if (a0 == null) {
        a0 = new SpecificData.Array<allezon.UserTag>((int)size0, SCHEMA$.getField("buys").schema());
        this.buys = a0;
      } else a0.clear();
      SpecificData.Array<allezon.UserTag> ga0 = (a0 instanceof SpecificData.Array ? (SpecificData.Array<allezon.UserTag>)a0 : null);
      for ( ; 0 < size0; size0 = in.arrayNext()) {
        for ( ; size0 != 0; size0--) {
          allezon.UserTag e0 = (ga0 != null ? ga0.peek() : null);
          if (e0 == null) {
            e0 = new allezon.UserTag();
          }
          e0.customDecode(in);
          a0.add(e0);
        }
      }

      long size1 = in.readArrayStart();
      java.util.List<allezon.UserTag> a1 = this.views;
      if (a1 == null) {
        a1 = new SpecificData.Array<allezon.UserTag>((int)size1, SCHEMA$.getField("views").schema());
        this.views = a1;
      } else a1.clear();
      SpecificData.Array<allezon.UserTag> ga1 = (a1 instanceof SpecificData.Array ? (SpecificData.Array<allezon.UserTag>)a1 : null);
      for ( ; 0 < size1; size1 = in.arrayNext()) {
        for ( ; size1 != 0; size1--) {
          allezon.UserTag e1 = (ga1 != null ? ga1.peek() : null);
          if (e1 == null) {
            e1 = new allezon.UserTag();
          }
          e1.customDecode(in);
          a1.add(e1);
        }
      }

    } else {
      for (int i = 0; i < 3; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.cookie = in.readString(this.cookie instanceof Utf8 ? (Utf8)this.cookie : null);
          break;

        case 1:
          long size0 = in.readArrayStart();
          java.util.List<allezon.UserTag> a0 = this.buys;
          if (a0 == null) {
            a0 = new SpecificData.Array<allezon.UserTag>((int)size0, SCHEMA$.getField("buys").schema());
            this.buys = a0;
          } else a0.clear();
          SpecificData.Array<allezon.UserTag> ga0 = (a0 instanceof SpecificData.Array ? (SpecificData.Array<allezon.UserTag>)a0 : null);
          for ( ; 0 < size0; size0 = in.arrayNext()) {
            for ( ; size0 != 0; size0--) {
              allezon.UserTag e0 = (ga0 != null ? ga0.peek() : null);
              if (e0 == null) {
                e0 = new allezon.UserTag();
              }
              e0.customDecode(in);
              a0.add(e0);
            }
          }
          break;

        case 2:
          long size1 = in.readArrayStart();
          java.util.List<allezon.UserTag> a1 = this.views;
          if (a1 == null) {
            a1 = new SpecificData.Array<allezon.UserTag>((int)size1, SCHEMA$.getField("views").schema());
            this.views = a1;
          } else a1.clear();
          SpecificData.Array<allezon.UserTag> ga1 = (a1 instanceof SpecificData.Array ? (SpecificData.Array<allezon.UserTag>)a1 : null);
          for ( ; 0 < size1; size1 = in.arrayNext()) {
            for ( ; size1 != 0; size1--) {
              allezon.UserTag e1 = (ga1 != null ? ga1.peek() : null);
              if (e1 == null) {
                e1 = new allezon.UserTag();
              }
              e1.customDecode(in);
              a1.add(e1);
            }
          }
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










