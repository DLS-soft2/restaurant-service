package com.dls.restaurantservice;


import com.dls.restaurantservice.Document.Restaurant;
import com.dls.restaurantservice.Document.MenuItem;
import com.dls.restaurantservice.Repository.MenuItemRepository;
import com.dls.restaurantservice.Repository.RestaurantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class InitData implements CommandLineRunner {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public InitData(RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Applikationen starter!");


        // Kun seed hvis databasen er tom
        if (restaurantRepository.count() > 0) {
            System.out.println("Data eksisterer allerede - springer over seed data.");
            return;
        }

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


        // Menu items for restaurant 1
        MenuItem menuItem1 = new MenuItem();
        menuItem1.setName("Margherita");
        menuItem1.setDescription("Klassisk pizza med tomatsauce, mozzarella og frisk basilikum.");
        menuItem1.setPrice(79.99);
        menuItem1.setRestaurant(restaurant1);
        menuItemRepository.save(menuItem1);

        MenuItem menuItem2 = new MenuItem();
        menuItem2.setName("Pepperoni");
        menuItem2.setDescription("Pizza med tomatsauce, mozzarella og masser af pepperoni.");
        menuItem2.setPrice(89.99);
        menuItem2.setRestaurant(restaurant1);
        menuItemRepository.save(menuItem2);

        // Menu items for restaurant 2
        MenuItem menuItem3 = new MenuItem();
        menuItem3.setName("Pad Thai");
        menuItem3.setDescription("Klassisk thailandsk ret med risnudler, rejer, tofu, æg, jordnødder og en lækker tamarindsauce.");
        menuItem3.setPrice(99.99);
        menuItem3.setRestaurant(restaurant2);
        menuItemRepository.save(menuItem3);

        MenuItem menuItem4 = new MenuItem();
        menuItem4.setName("Green Curry");
        menuItem4.setDescription("Krydret thailandsk karryret med kokosmælk, grøn karrypasta, kylling, aubergine og basilikum.");
        menuItem4.setPrice(109.99);
        menuItem4.setRestaurant(restaurant2);
        menuItemRepository.save(menuItem4);



        System.out.println("Initiel data gemt til databasen!");
    }
}