const { index, store, update, destroy } = require('./fruitController');

const main = () => {
    console.log(`Method index - Menampilkan buah`);
    index();
    store('Strawberry');
    update(0, 'Kedondong');
    destroy(0);
};

main();