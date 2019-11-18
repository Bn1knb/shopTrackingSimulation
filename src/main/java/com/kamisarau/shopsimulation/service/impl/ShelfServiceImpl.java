package com.kamisarau.shopsimulation.service.impl;

import com.kamisarau.shopsimulation.exception.NoProductFound;
import com.kamisarau.shopsimulation.model.Shelf;
import com.kamisarau.shopsimulation.model.WrappedProduct;
import com.kamisarau.shopsimulation.repository.ShelfRepository;
import com.kamisarau.shopsimulation.service.ShelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShelfServiceImpl implements ShelfService {
    private ShelfRepository shelfRepository;

    @Autowired
    public ShelfServiceImpl(ShelfRepository shelfRepository) {
        this.shelfRepository = shelfRepository;
    }

    @Override
    public WrappedProduct removeProduct(int index, Shelf shelf) throws NoProductFound {
        WrappedProduct removed = Optional.of(
                shelf.getProducts().remove(index)
        ).orElseThrow(NoProductFound::new);

        shelfRepository.save(shelf);

        return removed;
    }

    @Override
    public WrappedProduct removeProduct(String name, Shelf shelf) throws NoProductFound {
        WrappedProduct removed = shelf.getProducts()
                .stream()
                .filter(wrappedProduct -> wrappedProduct.getName().equals(name))
                .findFirst()
                .map(
                        p -> {
                            shelf.getProducts().remove(p);
                            return p;
                        }
                ).orElseThrow(NoProductFound::new);
        shelfRepository.save(shelf);
        return removed;
    }

    @Override
    public WrappedProduct storeProduct(WrappedProduct product, Shelf shelf) {
        if(shelf.getProducts() == null) {
            shelf.setProducts(new ArrayList<>());
        }

        shelf.store(product);
        shelfRepository.save(shelf);
        return product;
    }

    @Override
    public List<WrappedProduct> storeProduct(List<WrappedProduct> products, Shelf shelf) {
        if(shelf.getProducts() == null) {
            shelf.setProducts(new ArrayList<>());
        }

        products.forEach(shelf::store);
        shelfRepository.save(shelf);
        return products;
    }
}
