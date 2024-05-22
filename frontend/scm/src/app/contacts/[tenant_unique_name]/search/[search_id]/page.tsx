import ContactsPage from "@/app/contacts/[tenant_unique_name]/page";
import 'react-toastify/dist/ReactToastify.css';
const SearchPage = (props: { params: { tenant_unique_name: string, search_id: string } }) => {
    const { params } = props;
    const { tenant_unique_name, search_id } = params;

    return (
        <ContactsPage params={{ tenant_unique_name, search_id }} />
    );
}

export default SearchPage;