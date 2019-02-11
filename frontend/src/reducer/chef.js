export default function chef(state = [], action) {
    switch (action.type) {
        case 'ADD_ALL_CHEFS': {
            state = action.payload;
            break;
        }
        case 'ADD_CHEF': {
            return [
                ...state,
                action.payload
            ]
        }
        default:
            break;
    }
    return state;
}
