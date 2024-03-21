import {
  Card,
  Image,
  Text,
  Button,
  CardBody,
  CardFooter,
  Heading,
  Center,
} from "@chakra-ui/react";

const ClubCard = (clubs) => {
  console.log(clubs);
  return (
    
    <Center>
      {clubs.map((club)=>{

      <Card maxW="sm">
        <CardBody paddingBottom="0">
          <Image
            src="./assets/images/campusconnect.jpeg"
            alt="Club"
            borderRadius="lg"
          />

          <Heading size="md" paddingTop="10px">{club.club_name}</Heading>
          <Text>
            {club.description}
          </Text>
        </CardBody>
        <Center>
          <CardFooter style={{margin:0}}>
            <Button variant="solid" colorScheme="blue">
              View Details
            </Button>
          </CardFooter>
        </Center>
      </Card>
      })}
    </Center>
  );
};

export default ClubCard;
