package com.dls.restaurantservice;


import com.dls.restaurantservice.Entity.Restaurant;
import com.dls.restaurantservice.Repository.RestaurantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitData implements CommandLineRunner {

    private final RestaurantRepository restaurantRepository;

    public InitData(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Applikationen starter!");

        // Restaurant 1
        Restaurant restaurant1 = new Restaurant();
        restaurant1.setName("Pops Pizza");
        restaurant1.setAddress("Kongedalevej 25");
        restaurant1.setPhoneNumber("42875321");
        restaurant1.setEmail("PopsPizza1@gmail.com");
        restaurant1.setDescription("Pops Pizza er en hyggelig og familievenlig pizzarestaurant, der tilbyder et bredt udvalg af lækre pizzaer lavet med friske ingredienser. Vores menu inkluderer klassiske pizzaer som Margherita og Pepperoni, samt unikke kreationer som BBQ Chicken og Veggie Delight. Vi har også glutenfri og veganske muligheder for at imødekomme alle smagspræferencer. Vores venlige personale og afslappede atmosfære gør det til det perfekte sted at nyde en lækker pizza med venner og familie. Kom og oplev den autentiske smag af vores håndlavede pizzaer, og lad os forkæle dine smagsløg med vores passion for pizza!");
        restaurant1.setOpeningHours("Mandag-Fredag: 11:00-22:00, Lørdag-Søndag: 12:00-23:00");
        restaurant1.setIsOpen(true);
        restaurant1.setIsAvailable(true);
        restaurantRepository.save(restaurant1);


        // Restaurant 2
        Restaurant restaurant2 = new Restaurant();
        restaurant2.setName("Baan Pad Thai");
        restaurant2.setAddress("Sønder Boulevard 72");
        restaurant2.setPhoneNumber("42426654");
        restaurant2.setEmail("BaanPad@gmail.com");
        restaurant2.setDescription("Baan Pad Thai er en autentisk thailandsk restaurant, der specialiserer sig i den klassiske ret Pad Thai. Vores menu byder på en række lækre thailandske retter, herunder vores signatur Pad Thai, som er lavet med friske risnudler, saftige rejer, sprøde jordnødder og en perfekt balance af søde, sure og krydrede smagsnuancer. Vi tilbyder også vegetariske og veganske muligheder for at imødekomme alle kostbehov. Vores hyggelige atmosfære og venlige personale gør det til det ideelle sted at nyde en autentisk thailandsk madoplevelse. Kom og smag på vores lækre retter, og lad os tage dig med på en kulinarisk rejse til Thailand!");
        restaurant2.setOpeningHours("Mandag-Fredag: 11:00-21:00, Lørdag-Søndag: 12:00-22:00");
        restaurant2.setIsOpen(true);
        restaurant2.setIsAvailable(true);
        restaurantRepository.save(restaurant2);

        System.out.println("Initiel data gemt til databasen!");
    }
}