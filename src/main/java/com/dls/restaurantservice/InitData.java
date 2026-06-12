package com.dls.restaurantservice;


import com.dls.restaurantservice.Document.Restaurant;
import com.dls.restaurantservice.Document.MenuItem;
import com.dls.restaurantservice.Repository.MenuItemRepository;
import com.dls.restaurantservice.Repository.RestaurantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.UUID;

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
        System.out.println("Application starting!");

        if (restaurantRepository.count() > 0) {
            System.out.println("Data already exists - skipping seed data.");
            return;
        }

        Restaurant restaurant1 = new Restaurant();
        restaurant1.setName("Pops Pizza");
        restaurant1.setAddress("Kongedalevej 25");
        restaurant1.setPhoneNumber("42875321");
        restaurant1.setEmail("PopsPizza1@gmail.com");
        restaurant1.setDescription("Pops Pizza is a cozy, family-friendly pizzeria offering hand-crafted pizzas with fresh ingredients. From classic Margherita to creative specialties, there is something for everyone.");
        restaurant1.setOpeningHours("Mon-Fri: 11:00-22:00, Sat-Sun: 12:00-23:00");
        restaurant1.setIsOpen(true);
        restaurant1.setIsAvailable(true);
        restaurant1.setEstimatedPrepTimeMinutes(20);
        restaurant1.setRestaurantId("550e8400-e29b-41d4-a716-446655440001");
        restaurant1.setKeycloakId("aaaaaaaa-1111-2222-3333-000000000003");
        restaurant1.setCreatedAt(LocalDateTime.now());
        restaurant1.setUpdatedAt(LocalDateTime.now());
        restaurantRepository.save(restaurant1);


        Restaurant restaurant2 = new Restaurant();
        restaurant2.setName("Baan Pad Thai");
        restaurant2.setAddress("Sønder Boulevard 72");
        restaurant2.setPhoneNumber("42426654");
        restaurant2.setEmail("BaanPad@gmail.com");
        restaurant2.setDescription("Baan Pad Thai serves authentic Thai cuisine in the heart of Copenhagen. Our chefs prepare every dish with traditional recipes, fresh herbs, and bold Southeast Asian flavours.");
        restaurant2.setOpeningHours("Mon-Fri: 11:00-21:00, Sat-Sun: 12:00-22:00");
        restaurant2.setIsOpen(true);
        restaurant2.setIsAvailable(true);
        restaurant2.setEstimatedPrepTimeMinutes(25);
        restaurant2.setRestaurantId("550e8400-e29b-41d4-a716-446655440002");
        restaurant2.setKeycloakId("aaaaaaaa-1111-2222-3333-d00000000001");
        restaurant2.setCreatedAt(LocalDateTime.now());
        restaurant2.setUpdatedAt(LocalDateTime.now());
        restaurantRepository.save(restaurant2);

        Restaurant restaurant3 = new Restaurant();
        restaurant3.setName("Sakura Sushi");
        restaurant3.setAddress("Gothersgade 15, Copenhagen");
        restaurant3.setPhoneNumber("33445566");
        restaurant3.setEmail("sakura@restaurant.dls");
        restaurant3.setDescription("Sakura Sushi brings the art of Japanese cuisine to Copenhagen. Our skilled chefs prepare fresh nigiri, creative maki rolls, and traditional soups using the finest ingredients sourced daily.");
        restaurant3.setOpeningHours("Mon-Fri: 11:30-21:30, Sat-Sun: 12:00-22:00");
        restaurant3.setIsOpen(true);
        restaurant3.setIsAvailable(true);
        restaurant3.setEstimatedPrepTimeMinutes(30);
        restaurant3.setRestaurantId("550e8400-e29b-41d4-a716-446655440003");
        restaurant3.setKeycloakId("aaaaaaaa-1111-2222-3333-d00000000002");
        restaurant3.setCreatedAt(LocalDateTime.now());
        restaurant3.setUpdatedAt(LocalDateTime.now());
        restaurantRepository.save(restaurant3);

        Restaurant restaurant4 = new Restaurant();
        restaurant4.setName("Viking Burger");
        restaurant4.setAddress("Vesterbrogade 42, Copenhagen");
        restaurant4.setPhoneNumber("33445567");
        restaurant4.setEmail("viking@restaurant.dls");
        restaurant4.setDescription("Viking Burger serves hearty, hand-crafted burgers inspired by Nordic traditions. Our beef is locally sourced and each patty is seasoned with our secret Viking spice blend.");
        restaurant4.setOpeningHours("Mon-Fri: 11:00-22:00, Sat-Sun: 11:00-23:00");
        restaurant4.setIsOpen(true);
        restaurant4.setIsAvailable(true);
        restaurant4.setEstimatedPrepTimeMinutes(15);
        restaurant4.setRestaurantId("550e8400-e29b-41d4-a716-446655440004");
        restaurant4.setKeycloakId("aaaaaaaa-1111-2222-3333-d00000000003");
        restaurant4.setCreatedAt(LocalDateTime.now());
        restaurant4.setUpdatedAt(LocalDateTime.now());
        restaurantRepository.save(restaurant4);

        Restaurant restaurant5 = new Restaurant();
        restaurant5.setName("Curry House");
        restaurant5.setAddress("Nørrebrogade 88, Copenhagen");
        restaurant5.setPhoneNumber("33445568");
        restaurant5.setEmail("curry@restaurant.dls");
        restaurant5.setDescription("Curry House offers authentic Indian cuisine with a wide range of curries, tandoori dishes, and freshly baked naan bread. Our recipes are passed down through generations of master chefs.");
        restaurant5.setOpeningHours("Mon-Fri: 12:00-22:00, Sat-Sun: 12:00-23:00");
        restaurant5.setIsOpen(true);
        restaurant5.setIsAvailable(true);
        restaurant5.setEstimatedPrepTimeMinutes(25);
        restaurant5.setRestaurantId("550e8400-e29b-41d4-a716-446655440005");
        restaurant5.setKeycloakId("aaaaaaaa-1111-2222-3333-d00000000004");
        restaurant5.setCreatedAt(LocalDateTime.now());
        restaurant5.setUpdatedAt(LocalDateTime.now());
        restaurantRepository.save(restaurant5);


        // Pops Pizza menu items
        MenuItem menuItem1 = new MenuItem();
        menuItem1.setMenuItemId(UUID.randomUUID().toString());
        menuItem1.setName("Margherita");
        menuItem1.setDescription("Classic pizza with tomato sauce, mozzarella, and fresh basil");
        menuItem1.setPrice(79.95);
        menuItem1.setRestaurant(restaurant1);
        menuItemRepository.save(menuItem1);

        MenuItem menuItem2 = new MenuItem();
        menuItem2.setMenuItemId(UUID.randomUUID().toString());
        menuItem2.setName("Pepperoni");
        menuItem2.setDescription("Tomato sauce, mozzarella, and generous pepperoni slices");
        menuItem2.setPrice(89.95);
        menuItem2.setRestaurant(restaurant1);
        menuItemRepository.save(menuItem2);

        MenuItem menuItem3 = new MenuItem();
        menuItem3.setMenuItemId(UUID.randomUUID().toString());
        menuItem3.setName("Hawaiian");
        menuItem3.setDescription("Ham, pineapple, mozzarella, and tomato sauce");
        menuItem3.setPrice(89.95);
        menuItem3.setRestaurant(restaurant1);
        menuItemRepository.save(menuItem3);

        MenuItem menuItem4 = new MenuItem();
        menuItem4.setMenuItemId(UUID.randomUUID().toString());
        menuItem4.setName("BBQ Chicken");
        menuItem4.setDescription("Grilled chicken, red onion, BBQ sauce, and mozzarella");
        menuItem4.setPrice(99.95);
        menuItem4.setRestaurant(restaurant1);
        menuItemRepository.save(menuItem4);

        MenuItem menuItem5 = new MenuItem();
        menuItem5.setMenuItemId(UUID.randomUUID().toString());
        menuItem5.setName("Quattro Formaggi");
        menuItem5.setDescription("Mozzarella, gorgonzola, parmesan, and fontina on a white base");
        menuItem5.setPrice(99.95);
        menuItem5.setRestaurant(restaurant1);
        menuItemRepository.save(menuItem5);

        MenuItem menuItem6 = new MenuItem();
        menuItem6.setMenuItemId(UUID.randomUUID().toString());
        menuItem6.setName("Calzone");
        menuItem6.setDescription("Folded pizza stuffed with ham, mushrooms, and ricotta");
        menuItem6.setPrice(109.95);
        menuItem6.setRestaurant(restaurant1);
        menuItemRepository.save(menuItem6);

        MenuItem menuItem7 = new MenuItem();
        menuItem7.setMenuItemId(UUID.randomUUID().toString());
        menuItem7.setName("Garlic Bread");
        menuItem7.setDescription("Oven-baked bread with garlic butter and herbs");
        menuItem7.setPrice(39.95);
        menuItem7.setRestaurant(restaurant1);
        menuItemRepository.save(menuItem7);

        // Baan Pad Thai menu items
        MenuItem menuItem8 = new MenuItem();
        menuItem8.setMenuItemId(UUID.randomUUID().toString());
        menuItem8.setName("Pad Thai");
        menuItem8.setDescription("Stir-fried rice noodles with shrimp, tofu, egg, and peanuts");
        menuItem8.setPrice(99.95);
        menuItem8.setRestaurant(restaurant2);
        menuItemRepository.save(menuItem8);

        MenuItem menuItem9 = new MenuItem();
        menuItem9.setMenuItemId(UUID.randomUUID().toString());
        menuItem9.setName("Green Curry");
        menuItem9.setDescription("Spicy coconut curry with chicken, aubergine, and Thai basil");
        menuItem9.setPrice(109.95);
        menuItem9.setRestaurant(restaurant2);
        menuItemRepository.save(menuItem9);

        MenuItem menuItem10 = new MenuItem();
        menuItem10.setMenuItemId(UUID.randomUUID().toString());
        menuItem10.setName("Red Curry");
        menuItem10.setDescription("Rich red curry with beef, bamboo shoots, and bell peppers");
        menuItem10.setPrice(109.95);
        menuItem10.setRestaurant(restaurant2);
        menuItemRepository.save(menuItem10);

        MenuItem menuItem11 = new MenuItem();
        menuItem11.setMenuItemId(UUID.randomUUID().toString());
        menuItem11.setName("Tom Yum Soup");
        menuItem11.setDescription("Hot and sour soup with prawns, lemongrass, and galangal");
        menuItem11.setPrice(79.95);
        menuItem11.setRestaurant(restaurant2);
        menuItemRepository.save(menuItem11);

        MenuItem menuItem12 = new MenuItem();
        menuItem12.setMenuItemId(UUID.randomUUID().toString());
        menuItem12.setName("Spring Rolls");
        menuItem12.setDescription("Crispy vegetable rolls served with sweet chili sauce");
        menuItem12.setPrice(49.95);
        menuItem12.setRestaurant(restaurant2);
        menuItemRepository.save(menuItem12);

        MenuItem menuItem13 = new MenuItem();
        menuItem13.setMenuItemId(UUID.randomUUID().toString());
        menuItem13.setName("Mango Sticky Rice");
        menuItem13.setDescription("Sweet sticky rice with fresh mango and coconut cream");
        menuItem13.setPrice(59.95);
        menuItem13.setRestaurant(restaurant2);
        menuItemRepository.save(menuItem13);

        MenuItem menuItem14 = new MenuItem();
        menuItem14.setMenuItemId(UUID.randomUUID().toString());
        menuItem14.setName("Thai Fried Rice");
        menuItem14.setDescription("Wok-fried jasmine rice with chicken, egg, and vegetables");
        menuItem14.setPrice(89.95);
        menuItem14.setRestaurant(restaurant2);
        menuItemRepository.save(menuItem14);

        // Sakura Sushi menu items
        MenuItem menuItem15 = new MenuItem();
        menuItem15.setMenuItemId(UUID.randomUUID().toString());
        menuItem15.setName("Salmon Nigiri");
        menuItem15.setDescription("Fresh Atlantic salmon on seasoned rice");
        menuItem15.setPrice(89.95);
        menuItem15.setRestaurant(restaurant3);
        menuItemRepository.save(menuItem15);

        MenuItem menuItem16 = new MenuItem();
        menuItem16.setMenuItemId(UUID.randomUUID().toString());
        menuItem16.setName("Dragon Roll");
        menuItem16.setDescription("Tempura shrimp, avocado, and eel sauce");
        menuItem16.setPrice(119.95);
        menuItem16.setRestaurant(restaurant3);
        menuItemRepository.save(menuItem16);

        MenuItem menuItem17 = new MenuItem();
        menuItem17.setMenuItemId(UUID.randomUUID().toString());
        menuItem17.setName("California Roll");
        menuItem17.setDescription("Crab, avocado, and cucumber wrapped in soy paper");
        menuItem17.setPrice(79.95);
        menuItem17.setRestaurant(restaurant3);
        menuItemRepository.save(menuItem17);

        MenuItem menuItem18 = new MenuItem();
        menuItem18.setMenuItemId(UUID.randomUUID().toString());
        menuItem18.setName("Miso Soup");
        menuItem18.setDescription("Traditional soybean soup with tofu and seaweed");
        menuItem18.setPrice(39.95);
        menuItem18.setRestaurant(restaurant3);
        menuItemRepository.save(menuItem18);

        MenuItem menuItem19 = new MenuItem();
        menuItem19.setMenuItemId(UUID.randomUUID().toString());
        menuItem19.setName("Edamame");
        menuItem19.setDescription("Steamed soybeans with sea salt");
        menuItem19.setPrice(35.95);
        menuItem19.setRestaurant(restaurant3);
        menuItemRepository.save(menuItem19);

        MenuItem menuItem20 = new MenuItem();
        menuItem20.setMenuItemId(UUID.randomUUID().toString());
        menuItem20.setName("Tempura Udon");
        menuItem20.setDescription("Thick wheat noodles in dashi broth with shrimp tempura");
        menuItem20.setPrice(109.95);
        menuItem20.setRestaurant(restaurant3);
        menuItemRepository.save(menuItem20);

        MenuItem menuItem21 = new MenuItem();
        menuItem21.setMenuItemId(UUID.randomUUID().toString());
        menuItem21.setName("Sashimi Platter");
        menuItem21.setDescription("Assorted fresh fish slices: salmon, tuna, and yellowtail");
        menuItem21.setPrice(169.95);
        menuItem21.setRestaurant(restaurant3);
        menuItemRepository.save(menuItem21);

        // Viking Burger menu items
        MenuItem menuItem22 = new MenuItem();
        menuItem22.setMenuItemId(UUID.randomUUID().toString());
        menuItem22.setName("Classic Viking");
        menuItem22.setDescription("200g beef patty, cheddar, pickles, and secret sauce");
        menuItem22.setPrice(99.95);
        menuItem22.setRestaurant(restaurant4);
        menuItemRepository.save(menuItem22);

        MenuItem menuItem23 = new MenuItem();
        menuItem23.setMenuItemId(UUID.randomUUID().toString());
        menuItem23.setName("BBQ Bacon Burger");
        menuItem23.setDescription("Smoked bacon, BBQ sauce, and crispy onion rings");
        menuItem23.setPrice(119.95);
        menuItem23.setRestaurant(restaurant4);
        menuItemRepository.save(menuItem23);

        MenuItem menuItem24 = new MenuItem();
        menuItem24.setMenuItemId(UUID.randomUUID().toString());
        menuItem24.setName("Mushroom Swiss");
        menuItem24.setDescription("Sauteed mushrooms and melted Swiss cheese on beef patty");
        menuItem24.setPrice(109.95);
        menuItem24.setRestaurant(restaurant4);
        menuItemRepository.save(menuItem24);

        MenuItem menuItem25 = new MenuItem();
        menuItem25.setMenuItemId(UUID.randomUUID().toString());
        menuItem25.setName("Chicken Burger");
        menuItem25.setDescription("Crispy chicken breast with lettuce and garlic mayo");
        menuItem25.setPrice(99.95);
        menuItem25.setRestaurant(restaurant4);
        menuItemRepository.save(menuItem25);

        MenuItem menuItem26 = new MenuItem();
        menuItem26.setMenuItemId(UUID.randomUUID().toString());
        menuItem26.setName("Sweet Potato Fries");
        menuItem26.setDescription("Crispy sweet potato fries with chipotle dip");
        menuItem26.setPrice(39.95);
        menuItem26.setRestaurant(restaurant4);
        menuItemRepository.save(menuItem26);

        MenuItem menuItem27 = new MenuItem();
        menuItem27.setMenuItemId(UUID.randomUUID().toString());
        menuItem27.setName("Milkshake");
        menuItem27.setDescription("Thick and creamy vanilla, chocolate, or strawberry shake");
        menuItem27.setPrice(45.95);
        menuItem27.setRestaurant(restaurant4);
        menuItemRepository.save(menuItem27);

        // Curry House menu items
        MenuItem menuItem28 = new MenuItem();
        menuItem28.setMenuItemId(UUID.randomUUID().toString());
        menuItem28.setName("Butter Chicken");
        menuItem28.setDescription("Creamy tomato-based curry with tender chicken");
        menuItem28.setPrice(109.95);
        menuItem28.setRestaurant(restaurant5);
        menuItemRepository.save(menuItem28);

        MenuItem menuItem29 = new MenuItem();
        menuItem29.setMenuItemId(UUID.randomUUID().toString());
        menuItem29.setName("Lamb Vindaloo");
        menuItem29.setDescription("Spicy Goan curry with slow-cooked tender lamb");
        menuItem29.setPrice(129.95);
        menuItem29.setRestaurant(restaurant5);
        menuItemRepository.save(menuItem29);

        MenuItem menuItem30 = new MenuItem();
        menuItem30.setMenuItemId(UUID.randomUUID().toString());
        menuItem30.setName("Chicken Tikka Masala");
        menuItem30.setDescription("Grilled chicken in a rich and creamy spiced sauce");
        menuItem30.setPrice(109.95);
        menuItem30.setRestaurant(restaurant5);
        menuItemRepository.save(menuItem30);

        MenuItem menuItem31 = new MenuItem();
        menuItem31.setMenuItemId(UUID.randomUUID().toString());
        menuItem31.setName("Vegetable Biryani");
        menuItem31.setDescription("Fragrant basmati rice with mixed vegetables and spices");
        menuItem31.setPrice(89.95);
        menuItem31.setRestaurant(restaurant5);
        menuItemRepository.save(menuItem31);

        MenuItem menuItem32 = new MenuItem();
        menuItem32.setMenuItemId(UUID.randomUUID().toString());
        menuItem32.setName("Garlic Naan");
        menuItem32.setDescription("Freshly baked garlic flatbread from the tandoor");
        menuItem32.setPrice(29.95);
        menuItem32.setRestaurant(restaurant5);
        menuItemRepository.save(menuItem32);

        MenuItem menuItem33 = new MenuItem();
        menuItem33.setMenuItemId(UUID.randomUUID().toString());
        menuItem33.setName("Samosas");
        menuItem33.setDescription("Crispy pastry triangles filled with spiced potatoes and peas");
        menuItem33.setPrice(45.95);
        menuItem33.setRestaurant(restaurant5);
        menuItemRepository.save(menuItem33);

        MenuItem menuItem34 = new MenuItem();
        menuItem34.setMenuItemId(UUID.randomUUID().toString());
        menuItem34.setName("Mango Lassi");
        menuItem34.setDescription("Chilled yoghurt drink blended with fresh mango");
        menuItem34.setPrice(35.95);
        menuItem34.setRestaurant(restaurant5);
        menuItemRepository.save(menuItem34);

        System.out.println("Initial data saved to database!");
    }
}
