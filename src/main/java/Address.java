
public class Address {

    private String address;
    private String city;
    private int postalCode;

    public Address(String address, String city, int postalCode) {
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return address + ", " + city + ", " + postalCode;
    }

}
